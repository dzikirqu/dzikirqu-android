package com.wagyufari.dzikirqu.ui.read.quran.fragments.surah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.base.BaseNavigator
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.databinding.FragmentReadQuranBinding
import com.wagyufari.dzikirqu.databinding.TabTextBinding
import com.wagyufari.dzikirqu.model.events.CurrentAyahEvent
import com.wagyufari.dzikirqu.ui.adapters.FragmentPagerAdapter
import com.wagyufari.dzikirqu.ui.bsd.khatam.KhatamFooterBSD
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsBSD
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsConstants
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import com.wagyufari.dzikirqu.ui.read.quran.AyahQuery
import com.wagyufari.dzikirqu.ui.read.quran.AyahQueryType
import com.wagyufari.dzikirqu.ui.read.quran.ReadQuranNavigator
import com.wagyufari.dzikirqu.ui.read.quran.ReadQuranViewModel
import com.wagyufari.dzikirqu.ui.read.quran.listed.QuranListFragment
import com.wagyufari.dzikirqu.util.Appbar
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.StringExt.getText
import com.wagyufari.dzikirqu.util.ViewUtils.hide
import com.wagyufari.dzikirqu.util.openPagedQuran
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ReadQuranSurahFragment(
    val surahId: Int? = null,
    val juz: Int? = null,
    val hizb: Float? = null,
    val page: Int? = null,
    val ayah: Int? = null,
    val isNoteDeeplink: Boolean? = false,
) :
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

        surahId?.let { surahId ->
            ayah?.let { ayah ->
                configureSurah(surahId, ayah)
                RxBus.getDefault().send(CurrentAyahEvent(ayah, surahId))
            }
        }
        setUpTargetMotion()
    }


    fun configureSurah(surahId: Int, ayah: Int) {
        viewModel.surah.observe(viewLifecycleOwner) {
            buildVisibility()
            viewDataBinding?.verticalPager?.registerOnPageChangeCallback(onPageChangeSurah)
            val selectedSurah = it.reversed().filter { it.id == surahId }.firstOrNull()
            val fragments = it.reversed().map { surah ->
                if (surahId == surah.id) {
                    QuranListFragment.newInstance(
                        AyahQuery(
                            surah.id.toFloat(),
                            AyahQueryType.Surah
                        ), ayah, isNoteDeeplink
                    )
                } else {
                    QuranListFragment.newInstance(
                        AyahQuery(
                            surah.id.toFloat(),
                            AyahQueryType.Surah
                        ), 0, isNoteDeeplink
                    )
                }
            }
            viewDataBinding?.verticalPager?.apply {
                adapter = FragmentPagerAdapter(
                    requireActivity() as AppCompatActivity,
                    fragments.toCollection(arrayListOf())
                )
                setCurrentItem(it.reversed().indexOf(selectedSurah), false)
            }
            TabLayoutMediator(viewDataBinding?.tab!!,
                viewDataBinding?.verticalPager!!) { tab, position ->
                tab.customView =
                    TabTextBinding.inflate(LayoutInflater.from(requireActivity())).apply {
                        title.text = it.reversed()[position].name
                    }.root
            }.attach()
        }

        viewDataBinding?.appbar?.setContent {
            val title = viewModel.title.observeAsState()
            val subtitle = viewModel.subtitle.observeAsState()
            Appbar(rowModifier = Modifier.padding(horizontal = 16.dp,
                vertical = 12.dp)).withBackButton().setTitle(title.value.toString(), fontSize = 14)
                .setSubtitle(subtitle.value.toString(), fontSize = 12).setRightButton(
                    rightButtonImage = listOf(R.drawable.ic_settings),
                    rightButtonHandler = listOf { onClickSettings() })
                .isCentered()
                .build()
        }

    }

    fun buildVisibility() {
        viewDataBinding?.pagedPager?.hide()
        viewDataBinding?.recycler?.hide()
    }


    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding?.verticalPager?.let {
            it.unregisterOnPageChangeCallback(onPageChangeSurah)
        }
    }

    val onPageChangeSurah = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val surah = viewModel.surah.value?.reversed()?.get(position)
            viewModel.title.value = surah?.name
            viewModel.subtitle.value = surah?.translation?.getText()
            viewModel.currentSurah.value = surah
        }
    }

    fun setUpTargetMotion() {
        viewDataBinding?.toggleBottomSheet?.setOnClickListener {
            KhatamFooterBSD.newInstance(viewModel.currentSurah.value?.id, viewModel.currentAyah.value).show(childFragmentManager, "")
        }
    }

    override fun onClickSettings() {
        SettingsBSD(arrayListOf(SettingsConstants.FONTS, SettingsConstants.QURAN)).show(
            childFragmentManager,
            ""
        )
    }

    override fun onClickPage() {
        requireActivity().openPagedQuran(viewModel.currentSurah.value?.id ?: 1,
            viewModel.currentAyah.value)
    }


    override fun onClickLastRead() {
        ReadActivity.startSurah(
            requireActivity(),
            Prefs.quranLastRead.surah,
            Prefs.quranLastRead.ayah
        )
        finish()
    }

}


