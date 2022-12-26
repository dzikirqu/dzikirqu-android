package com.dzikirqu.android.ui.khatam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.dzikirqu.android.BR
import com.dzikirqu.android.base.BaseActivity
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.data.room.dao.getKhatamDao
import com.dzikirqu.android.databinding.KhatamBsdBinding
import com.dzikirqu.android.model.*
import com.dzikirqu.android.ui.adapters.KhatamIterationAdapter
import com.dzikirqu.android.ui.khatam.composer.KhatamComposerBSD
import com.dzikirqu.android.ui.khatam.fragments.KhatamDetailFragment
import com.dzikirqu.android.ui.v2.theme.lato
import com.dzikirqu.android.util.nonRippleClickable
import com.dzikirqu.android.util.verticalSpacer
import dagger.hilt.android.AndroidEntryPoint
import io.grpc.InternalChannelz.id
import java.util.*
import javax.inject.Inject
import com.dzikirqu.android.R

@AndroidEntryPoint
class KhatamActivity : BaseActivity<KhatamBsdBinding, KhatamViewModel>(), KhatamNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.khatam_bsd
    override val viewModel: KhatamViewModel by viewModels()

    @Inject
    lateinit var khatamIterationAdapter: KhatamIterationAdapter

    lateinit var popup: PopupMenu

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, KhatamActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        viewDataBinding.lifecycleOwner = this
        viewModel.currentKhatam.observe(this) {
            it?.let {
                viewDataBinding.appbar.setContent {
                    val calendar = UmmalquraCalendar().apply {
                        it.startDate?.let {
                            time = it
                        }
                    }
                    val month = "${
                        calendar.getDisplayName(Calendar.MONTH,
                            Calendar.LONG,
                            Locale.ENGLISH)
                    } ${calendar.get(Calendar.YEAR)}"

                    Column(modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        verticalSpacer(16.dp)

                        Row {
                            Image(modifier = Modifier
                                .size(32.dp)
                                .nonRippleClickable {
                                    super.onBackPressed()
                                },
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    colorResource(id = R.color.neutral_600)))
                            Box(Modifier.weight(1f))
                            Image(modifier = Modifier
                                .size(28.dp)
                                .nonRippleClickable {
                                    viewModel.currentKhatam.value?.let {
                                        KhatamComposerBSD
                                            .newInstance(it)
                                            .show(supportFragmentManager, "")
                                    }
                                },
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    colorResource(id = R.color.neutral_600)))
                        }

                        verticalSpacer(24.dp)

                        viewModel.currentKhatam.observeAsState().value?.let { khatam ->
                            val currentProgress: Float =
                                ((khatam.currentProgress(context = this@KhatamActivity) * 100) / khatam.maxValue()).toFloat()

                            CircularProgress(currentProgress = currentProgress)

                            verticalSpacer(height = 16.dp)

                            Text(text = "${viewModel.hijriMonth.observeAsState().value} ${viewModel.hijriYear.observeAsState().value}",
                                fontFamily = lato,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold, color = colorResource(id = R.color.textPrimary))

                            verticalSpacer(height = 8)

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Card(modifier = Modifier,
                                    border = BorderStroke(1.dp,
                                        color = colorResource(id = R.color.neutral_200)),
                                    shape = RoundedCornerShape(4.dp),
                                    elevation = 0.dp, backgroundColor = colorResource(id = android.R.color.transparent)) {
                                    Text(modifier = Modifier.padding(horizontal = 12.dp,
                                        vertical = 6.dp),
                                        text = "Khattam ${khatam.iteration?.count()}x",
                                        fontFamily = lato,
                                        fontSize = 16.sp,
                                        color = colorResource(
                                            id = R.color.textTertiary))
                                }
                                Card(modifier = Modifier,
                                    border = BorderStroke(1.dp,
                                        color = colorResource(id = R.color.neutral_200)),
                                    shape = RoundedCornerShape(4.dp),
                                    elevation = 0.dp, backgroundColor = colorResource(id = android.R.color.transparent)) {
                                    Text(modifier = Modifier.padding(horizontal = 12.dp,
                                        vertical = 6.dp),
                                        text = "${LocaleConstants.REMIND.locale()} ${Prefs.khatamReminderType.toStringLocale()}",
                                        fontFamily = lato,
                                        fontSize = 16.sp,
                                        color = colorResource(
                                            id = R.color.textTertiary))
                                }
                            }

                            verticalSpacer(8)

                            DaysLeft(khatam = khatam)

                            verticalSpacer(height = 16)

                            khatam.iteration?.forEachIndexed { index, lastRead ->
                                KhatamIterationCard(onClick = {
                                    if (khatam.state != KhatamStateConstants.INACTIVE){
                                        lastRead.setAsActive(this@KhatamActivity, khatam)
                                    }
                                },lastRead = lastRead, index = index)
                            }
                            verticalSpacer(128.dp)
                        }
                    }
                }

                it.let {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, KhatamDetailFragment.newInstance(it))
                        .commit()
                }
            }
        }

        getKhatamDao().getKhatam().observe(this) { khatam ->
            val titles = khatam.map {
                viewModel.getMonthString(it)
            }

            popup = PopupMenu(this, viewDataBinding?.appbar!!)
            titles.forEach {
                popup.menu.add(it)
            }
            popup.setOnMenuItemClickListener { menu ->
                viewModel.currentKhatam.value =
                    khatam.first { viewModel.getMonthString(it) == menu.title }
                true
            }
            viewModel.currentKhatam.value = khatam.last()
        }
    }


}