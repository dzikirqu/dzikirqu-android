package com.wagyufari.dzikirqu.ui.main.quran

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.Insets
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.SeparatorItem
import com.wagyufari.dzikirqu.base.BaseFullDialog
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.dao.getQuranLastReadDao
import com.wagyufari.dzikirqu.data.room.dao.getSurahDao
import com.wagyufari.dzikirqu.databinding.FragmentMainQuranBinding
import com.wagyufari.dzikirqu.model.QuranLastRead
import com.wagyufari.dzikirqu.model.QuranLastReadString
import com.wagyufari.dzikirqu.ui.bsd.StartReadQuranBSD
import com.wagyufari.dzikirqu.ui.jump.JumpQuranActivity
import com.wagyufari.dzikirqu.ui.khatam.KhatamActivity
import com.wagyufari.dzikirqu.ui.main.bookmarks.BookmarkActivity
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import com.wagyufari.dzikirqu.ui.search.SearchActivity
import com.wagyufari.dzikirqu.ui.v2.theme.lato
import com.wagyufari.dzikirqu.ui.v2.theme.surah
import com.wagyufari.dzikirqu.util.Appbar
import com.wagyufari.dzikirqu.util.FileUtils
import com.wagyufari.dzikirqu.util.ViewUtils.height
import com.wagyufari.dzikirqu.util.horizontalSpacer
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainQuranFragment : BaseFullDialog<FragmentMainQuranBinding, MainQuranViewModel>(),
    MainQuranNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_main_quran
    override val viewModel: MainQuranViewModel by viewModels()

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager


    companion object {

        const val ARG_IS_NOTE_DEEPLINK = "arg_is_note_deeplink"

        fun newInstance(isNoteDeeplink: Boolean): MainQuranFragment {
            return MainQuranFragment().apply {
                arguments = bundleOf(ARG_IS_NOTE_DEEPLINK to isNoteDeeplink)
            }
        }

        fun MainQuranFragment.isNoteDeeplink(): Boolean {
            return arguments?.getBoolean(ARG_IS_NOTE_DEEPLINK, false) ?: false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.navigator = this
        setUpViews()
    }

    fun setUpViews() {
        viewDataBinding?.appbar?.isVisible = !isNoteDeeplink()

        viewDataBinding?.jump?.setOnClickListener {
            startActivity(JumpQuranActivity.newIntent(requireActivity()))
        }

        viewDataBinding?.statusBarHeight?.height(Prefs.statusBarHeight, duration = 0)

        viewDataBinding?.appbar?.setContent {
            Column {
                Appbar(backgroundColor = colorResource(id = android.R.color.transparent)).setTitle(
                    LocaleConstants.QURAN.locale(), fontWeight = FontWeight.Bold,
                    fontSize = 22
                ).setRightButton(
                    rightButtonImage = listOf(
                        R.drawable.ic_bookmarks,
                        R.drawable.ic_target
                    ), rightButtonHandler = listOf({
                        BookmarkActivity.startQuran(requireActivity())
                    }, {
                        KhatamActivity.start(requireActivity())
                    })
                ).setSearch(LocaleConstants.SEARCH_QURAN.locale()) {
                    startActivity(SearchActivity.newQuranIntent(requireActivity()))
                }
                    .setElevation(0)
                    .build()

                Box(Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.neutral_200))
                    .border(BorderStroke(1.dp, colorResource(
                        id = R.color.neutral_300)))) {
                    LazyRow(Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            horizontalSpacer(width = 22)
                            ItemLastReadAttachment(lastRead = Prefs.quranLastRead)
                            horizontalSpacer(width = 8.dp)
                            LinkedList<QuranLastRead>().apply {
                                addAll(getQuranLastReadDao().getQuranLastRead()
                                    .observeAsState().value ?: listOf())
                            }.forEach {
                                ItemLastRead(lastRead = it)
                                horizontalSpacer(width = 8.dp)
                            }
                        }
                    }
                }
            }
        }

        viewDataBinding?.surah?.isVisible = Prefs.quranFilterMode == 0
        viewDataBinding?.juz?.isVisible = Prefs.quranFilterMode == 1
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ItemLastReadAttachment(lastRead: QuranLastRead) {
        getSurahDao().getSurahByIdLive(lastRead.surah).observeAsState().value?.let {
            val data = QuranLastReadString(
                it.name,
                if (lastRead.isSavedFromPage == true) "${LocaleConstants.PAGE.locale()} ${lastRead.page}" else "${LocaleConstants.AYAH.locale()} ${lastRead.ayah}",
                getArabicCalligraphy(requireActivity(), lastRead.surah)
            )
            Card(backgroundColor = colorResource(id = R.color.colorPrimary),
                shape = RoundedCornerShape(10.dp),
                elevation = 1.dp) {
                Box(Modifier
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true),
                        onClick = {
                            StartReadQuranBSD
                                .newInstantInstance(surah = lastRead.surah,
                                    ayah = lastRead.ayah)
                                .show(childFragmentManager, "")
                        },
                        onLongClick = {
                            StartReadQuranBSD
                                .newSelectionInstance(surah = lastRead.surah, ayah = lastRead.ayah)
                                .show(childFragmentManager, "")
                        }
                    )) {
                    Row(
                        modifier = Modifier.padding(vertical = 14.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Image(modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                            painter = painterResource(id = R.drawable.ic_attachment),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                colorResource(id = R.color.white)))
                        horizontalSpacer(width = 8)
                        Column {
                            Text(text = data.surah,
                                fontFamily = lato,
                                fontSize = 14.sp,
                                color = colorResource(
                                    id = R.color.white))
                            Text(text = data.ayah,
                                fontFamily = lato,
                                fontSize = 12.sp,
                                color = colorResource(
                                    id = R.color.white))
                        }
                        horizontalSpacer(width = 16)
                        Text(text = data.unicode,
                            fontFamily = surah,
                            fontSize = 18.sp,
                            color = colorResource(
                                id = R.color.white))
                    }

                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ItemLastRead(lastRead: QuranLastRead) {
        getSurahDao().getSurahByIdLive(lastRead.surah).observeAsState().value?.let {
            val data = QuranLastReadString(
                it.name,
                if (lastRead.isSavedFromPage == true) "${LocaleConstants.PAGE.locale()} ${lastRead.page}" else "${LocaleConstants.AYAH.locale()} ${lastRead.ayah}",
                getArabicCalligraphy(requireActivity(), lastRead.surah)
            )
            Card(backgroundColor = colorResource(id = R.color.card),
                shape = RoundedCornerShape(10.dp),
                elevation = 1.dp) {
                Box(Modifier
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true),
                        onClick = {
                            lastRead.page?.let {
                                ReadActivity.startPage(requireActivity(), lastRead.page ?: 1)
                            } ?: kotlin.run {
                                StartReadQuranBSD
                                    .newInstantInstance(surah = lastRead.surah,
                                        ayah = lastRead.ayah)
                                    .show(childFragmentManager, "")
                            }
                        },
                        onLongClick = {
                            StartReadQuranBSD
                                .newSelectionInstance(surah = lastRead.surah, ayah = lastRead.ayah)
                                .show(childFragmentManager, "")
                        }
                    )){
                    Row(
                        modifier = Modifier.padding(vertical = 14.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        horizontalSpacer(width = 8)
                        Column {
                            Text(text = data.surah,
                                fontFamily = lato,
                                fontSize = 14.sp,
                                color = colorResource(
                                    id = R.color.textPrimary))
                            Text(text = data.ayah,
                                fontFamily = lato,
                                fontSize = 12.sp,
                                color = colorResource(
                                    id = R.color.textPrimary))
                        }
                        horizontalSpacer(width = 16)
                        Text(text = data.unicode,
                            fontFamily = surah,
                            fontSize = 18.sp,
                            color = colorResource(
                                id = R.color.textPrimary))
                    }

                }

            }
        }
    }

    fun getArabicCalligraphy(mContext: Context, surah: Int): String {
        return Gson().fromJson<ArrayList<SeparatorItem>>(
            FileUtils.getJsonStringFromAssets(
                mContext,
                "json/quran/paged/separator.json"
            ), object :
                TypeToken<ArrayList<SeparatorItem>>() {}.type
        ).filter { it.surah == surah }.firstOrNull()?.unicode ?: ""
    }

    override fun onSettingsEvent() {
        super.onSettingsEvent()
        viewDataBinding?.surah?.isVisible = Prefs.quranFilterMode == 0
        viewDataBinding?.juz?.isVisible = Prefs.quranFilterMode == 1
    }

    override fun onApplyWindowEvent(insets: Insets) {
        viewDataBinding?.statusBarHeight?.height(insets.top, duration = 0)
    }

    override fun onNotePropertySelectedEvent() {
    }

}