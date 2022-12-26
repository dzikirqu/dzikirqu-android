package com.dzikirqu.android.ui.read.quran.fragments.hizb

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.data.room.dao.getKhatamDao
import com.dzikirqu.android.data.room.dao.getQuranLastReadDao
import com.dzikirqu.android.databinding.FragmentReadQuranBinding
import com.dzikirqu.android.model.Ayah
import com.dzikirqu.android.model.KhatamStateConstants
import com.dzikirqu.android.model.QuranLastRead
import com.dzikirqu.android.model.events.CurrentAyahEvent
import com.dzikirqu.android.model.events.MenuEvent
import com.dzikirqu.android.model.events.QuranScrollEvent
import com.dzikirqu.android.model.update
import com.dzikirqu.android.ui.adapters.QuranListedAdapter
import com.dzikirqu.android.ui.bsd.khatam.KhatamFooterBSD
import com.dzikirqu.android.ui.bsd.quranmenu.QuranMenuBSD
import com.dzikirqu.android.ui.bsd.settings.SettingsBSD
import com.dzikirqu.android.ui.bsd.settings.SettingsConstants
import com.dzikirqu.android.ui.read.ReadActivity
import com.dzikirqu.android.ui.read.quran.AyahQuery
import com.dzikirqu.android.ui.read.quran.AyahQueryType
import com.dzikirqu.android.ui.read.quran.ReadQuranNavigator
import com.dzikirqu.android.ui.read.quran.ReadQuranViewModel
import com.dzikirqu.android.util.Appbar
import com.dzikirqu.android.util.RxBus
import com.dzikirqu.android.util.StringExt.hizb
import com.dzikirqu.android.util.ViewUtils.hide
import com.dzikirqu.android.util.io
import com.dzikirqu.android.util.main
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class ReadQuranHizbFragment(val hizb: Float? = null) :
    BaseFragment<FragmentReadQuranBinding, ReadQuranViewModel>(), BaseNavigator,
    ReadQuranNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_read_quran
    override val viewModel: ReadQuranViewModel by viewModels()

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager
    var currentLastRead: QuranLastRead? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.navigator = this

        viewDataBinding?.toggleBottomSheet?.hide()

        hizb?.let { configureHizb(it) }

        setUpTargetMotion()
    }

    fun configureHizb(hizb: Float) {
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
        viewModel.title.value = hizb.hizb()
        buildVisibility()
        io {
            val data = viewModel.getAyah(AyahQuery(hizb, AyahQueryType.Hizb))
            main {
                viewDataBinding?.recycler?.apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = QuranListedAdapter().apply {
                        addItems(data)
                        scheduleLayoutAnimation()
                        setListener(object : QuranListedAdapter.Callback {
                            override fun onSelectedItem(ayah: Ayah) {
                                QuranMenuBSD.newInstance(ayah.chapterId, ayah.verse_number).show(
                                    childFragmentManager,
                                    ""
                                )
                            }
                        })
                    }
                }

                viewDataBinding?.recycler?.addOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        RxBus.getDefault().send(QuranScrollEvent(dy))
                        getAyahFromPosition(data)?.let { ayah ->
                            RxBus.getDefault().send(CurrentAyahEvent(ayah.verse_number, surah = ayah.chapterId))
                            if (Prefs.isMarkQuranLastReadAutomatically) {
                                Prefs.quranLastRead = QuranLastRead(ayah.chapterId, ayah.verse_number)
                                RxBus.getDefault().send(MenuEvent.AutoLastRead)
                                setQuranLastRead(ayah.chapterId, ayah.verse_number)
                            }
                            currentLastRead = QuranLastRead(
                                ayah.chapterId,
                                ayah.verse_number,
                                timestamp = Calendar.getInstance().timeInMillis
                            )
                            updateCurrentLastRead()
                        }
                    }
                })
            }
        }
    }

    fun getAyahFromPosition(data: ArrayList<Any>): Ayah? {
        val position =
            (viewDataBinding?.recycler?.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        val ayah = data[if (position == -1) 0 else position]
        if (ayah is Ayah) {
            return ayah
        }
        return null
    }

    fun setQuranLastRead(surah:Int, ayah:Int){
        io{
            getKhatamDao().getKhatamByState(KhatamStateConstants.ACTIVE)?.update(surah,ayah,null)
                .let { it1 -> it1?.let { getKhatamDao().updateKhatam(it) } }
        }
    }

    fun updateCurrentLastRead() {
        currentLastRead?.let { lastRead ->
            lifecycleScope.launch {
                getQuranLastReadDao().putQuranLastRead(lastRead)
            }
        }
    }

    fun setUpTargetMotion(){
        viewDataBinding?.toggleBottomSheet?.setOnClickListener {
            KhatamFooterBSD.newInstance(viewModel.currentSurah.value?.id, viewModel.currentAyah.value).show(childFragmentManager, "")
        }
    }

    private fun buildVisibility() {
        viewDataBinding?.tab?.hide()
        viewDataBinding?.pagedPager?.hide()
    }

    override fun onClickSettings() {
        SettingsBSD(arrayListOf(SettingsConstants.FONTS, SettingsConstants.QURAN)).show(
            childFragmentManager,
            ""
        )
    }

    override fun onClickLastRead() {
        ReadActivity.startSurah(
            requireActivity(),
            Prefs.quranLastRead.surah,
            Prefs.quranLastRead.ayah
        )
        finish()
    }


    override fun onClickPage() {
    }

}


