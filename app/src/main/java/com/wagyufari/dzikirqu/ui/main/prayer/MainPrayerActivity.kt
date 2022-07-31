package com.wagyufari.dzikirqu.ui.main.prayer

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseActivity
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.databinding.FragmentMainPrayerBinding
import com.wagyufari.dzikirqu.databinding.TabImageCircleBinding
import com.wagyufari.dzikirqu.databinding.TabTextCircleBinding
import com.wagyufari.dzikirqu.model.LanguageString
import com.wagyufari.dzikirqu.model.events.MainTabEvent
import com.wagyufari.dzikirqu.model.events.MainTabType
import com.wagyufari.dzikirqu.ui.adapters.FragmentPagerAdapter
import com.wagyufari.dzikirqu.ui.main.bookmarks.BookmarkActivity
import com.wagyufari.dzikirqu.ui.main.prayer.fragments.grid.PrayerGridFragment
import com.wagyufari.dzikirqu.ui.main.prayer.fragments.list.PrayerListFragment
import com.wagyufari.dzikirqu.ui.search.SearchActivity
import com.wagyufari.dzikirqu.util.*
import com.wagyufari.dzikirqu.util.StringExt.getText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize


@AndroidEntryPoint
class MainPrayerActivity : BaseActivity<FragmentMainPrayerBinding, MainPrayerViewModel>(),
    MainPrayerNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_main_prayer
    override val viewModel: MainPrayerViewModel by viewModels()

    companion object {
        fun start(view: Context) {
            view.startActivity(Intent(view, MainPrayerActivity::class.java))
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = this
        viewDataBinding?.appbar?.setContent {
            println("${viewModel.pagePosition.value}")
            Column(
                modifier = Modifier,
            ) {
                Appbar(Modifier,
                    Modifier.padding(top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp)).setTitle(
                    LocaleConstants.PRAYER.locale(),
                    fontWeight = FontWeight.Black,
                    fontSize = 24
                ).withBackButton().setRightButton(
                    rightButtonImage = listOf(
                        R.drawable.ic_bookmarks
                    ), rightButtonHandler = listOf {
                        BookmarkActivity.startPrayer(this@MainPrayerActivity)
                    }
                ).setSearch(LocaleConstants.SEARCH_PRAYER.locale()) {
                    SearchActivity.startPrayer(this@MainPrayerActivity)
                }.setElevation(0).build()
                verticalSpacer(height = 0)
            }
        }

        viewDataBinding?.let { viewDataBinding ->
            viewDataBinding.pager.adapter =
                FragmentPagerAdapter(this as AppCompatActivity,
                    arrayListOf<Fragment>().apply {
                        add(PrayerGridFragment.newInstance())
                        addAll(prayerTypes.map { PrayerListFragment.newInstance(it) })
                    })

            TabLayoutMediator(viewDataBinding.tab, viewDataBinding.pager) { tab, position ->
                if (position == 0) {
                    tab.customView =
                        TabImageCircleBinding.inflate(LayoutInflater.from(this))
                            .apply {
                                image.setImageDrawable(ContextCompat.getDrawable(this@MainPrayerActivity,
                                    R.drawable.ic_grid))
                            }.root
                } else {
                    tab.customView =
                        TabTextCircleBinding.inflate(LayoutInflater.from(this)).apply {
                            title.text = prayerTypes[position - 1].title.getText()
                        }.root
                }

            }.attach()
        }

        viewDataBinding?.tab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position != 0) {
                    animateValue(8.dp(), 24.dp()) { b24 ->
                        tab?.customView?.setPadding(b24, 8.dp(), b24, 8.dp())
                    }

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab?.position != 0) {
                    animateValue(24.dp(), 8.dp()) { b24 ->
                        tab?.customView?.setPadding(b24, 8.dp(), b24, 8.dp())
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        viewDataBinding?.pager?.registerOnPageChangeCallback(pageChangeCallback)

        viewModel.pagePosition.observe(this) {
            viewDataBinding?.pager?.setCurrentItem(it, true)
        }
    }

    override fun onBackPressed() {
        if (viewModel.pagePosition.value != 0) {
            viewModel.pagePosition.value = 0
        } else {
            super.onBackPressed()
        }
    }

    fun Int.dp(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewModel.pagePosition.value = position
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding?.pager?.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    override fun onBackEvent() {
        if (viewModel.pagePosition.value != 0) {
            viewModel.pagePosition.value = 0
        } else {
            RxBus.getDefault().send(MainTabEvent(MainTabType.HOME))
        }
    }

}


@Parcelize
data class PrayerType(val title: ArrayList<LanguageString>, val ids: List<Int>, val image: Int) :
    Parcelable