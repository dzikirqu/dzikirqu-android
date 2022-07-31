package com.wagyufari.dzikirqu.ui.read.quran.fragments.paged

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.base.BaseNavigator
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.databinding.FragmentReadQuranBinding
import com.wagyufari.dzikirqu.ui.adapters.FragmentPagerAdapter
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsBSD
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsConstants
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import com.wagyufari.dzikirqu.ui.read.quran.ReadQuranNavigator
import com.wagyufari.dzikirqu.ui.read.quran.ReadQuranViewModel
import com.wagyufari.dzikirqu.ui.read.quran.paged.QuranPagedFragment
import com.wagyufari.dzikirqu.util.ViewUtils.fadeHide
import com.wagyufari.dzikirqu.util.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ReadQuranPagedFragment(val page: Int? = null) :
    BaseFragment<FragmentReadQuranBinding, ReadQuranViewModel>(), BaseNavigator,
    ReadQuranNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_read_quran
    override val viewModel: ReadQuranViewModel by viewModels()

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.navigator = this

        page?.let {
            configurePages(it)
        }
    }

    fun configurePages(page:Int){
        buildViews()
        viewDataBinding?.appbar?.fadeHide()
        viewDataBinding?.tab?.isVisible = false
        viewDataBinding?.pagedPager?.apply {
            val pages = arrayListOf<Int>().apply {
                for (i in 1..604) {
                    add(i)
                }
            }.reversed().toCollection(arrayListOf())
            val fragments = pages.map { page->
                QuranPagedFragment.newInstance(page)
            }
            adapter = FragmentPagerAdapter(requireActivity() as AppCompatActivity, fragments.toCollection(arrayListOf()))
            setCurrentItem(pages.indexOf(page), false)
        }
    }

    fun buildViews() {
        viewDataBinding?.pagedPager?.show()
//        viewDataBinding?.tab?.hide()
//        viewDataBinding?.appbar?.elevation = 2f
//        viewDataBinding?.recycler?.hide()
//        viewDataBinding?.progress?.hide()
//        viewDataBinding?.settings?.hide()
//        viewDataBinding?.footerContainer?.hide()
//        viewDataBinding?.footerShadow?.hide()
//        viewDataBinding?.lockContainer?.hide()
    }

    override fun onClickLastRead() {
        ReadActivity.startSurah(requireActivity(), Prefs.quranLastRead.surah, Prefs.quranLastRead.ayah)
        finish()
    }


    override fun onClickSettings() {
        SettingsBSD(arrayListOf(SettingsConstants.FONTS,SettingsConstants.QURAN)).show(childFragmentManager,"")
    }

    override fun onClickPage() {
    }

}


