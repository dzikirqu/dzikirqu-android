package com.wagyufari.dzikirqu.ui.read.hadith

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.constants.ReadModeConstants
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.databinding.FragmentReadHadithBinding
import com.wagyufari.dzikirqu.model.Hadith
import com.wagyufari.dzikirqu.model.HadithChapter
import com.wagyufari.dzikirqu.model.HadithSubChapter
import com.wagyufari.dzikirqu.model.events.NoteInsertEvent
import com.wagyufari.dzikirqu.ui.adapters.HadithReadAdapter
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsBSD
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsConstants
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.StringExt.getText
import com.wagyufari.dzikirqu.util.ViewUtils.animToY
import com.wagyufari.dzikirqu.util.ViewUtils.onClickAnimate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ReadHadithFragment :
    BaseFragment<FragmentReadHadithBinding, ReadHadithViewModel>(), ReadHadithNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_read_hadith
    override val viewModel: ReadHadithViewModel by viewModels()

    lateinit var hadithReadAdapterVertical: HadithReadAdapter
    lateinit var hadithReadAdapterPaged: HadithReadAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    companion object {
        const val ARG_HADITH = "arg_hadith"
        const val ARG_START_INDEX = "arg_start_index"
        const val ARG_END_INDEX = "arg_end_index"
        const val ARG_SUBTITLE = "arg_subtitle"
        const val ARG_IS_NOTE_DEEPLINK = "arg_is_note_deeplink"

        fun newInstance(hadith: Hadith?, isNoteDeeplink: Boolean = false): Fragment {
            return ReadHadithFragment().apply {
                arguments = bundleOf(ARG_HADITH to hadith, ARG_IS_NOTE_DEEPLINK to isNoteDeeplink)
            }
        }

        fun newInstance(
            hadith: Hadith?,
            chapter: HadithChapter?,
            isNoteDeeplink: Boolean = false
        ): Fragment {
            return ReadHadithFragment().apply {
                arguments = bundleOf(
                    ARG_HADITH to hadith,
                    ARG_START_INDEX to chapter?.startIndex,
                    ARG_END_INDEX to chapter?.endIndex,
                    ARG_SUBTITLE to chapter?.title?.getText(),
                    ARG_IS_NOTE_DEEPLINK to isNoteDeeplink
                )
            }
        }

        fun newInstance(
            hadith: Hadith?,
            chapter: HadithSubChapter?,
            isNoteDeeplink: Boolean = false
        ): Fragment {
            return ReadHadithFragment().apply {
                arguments = bundleOf(
                    ARG_HADITH to hadith,
                    ARG_START_INDEX to chapter?.startIndex,
                    ARG_END_INDEX to chapter?.endIndex,
                    ARG_SUBTITLE to chapter?.title?.getText(),
                    ARG_IS_NOTE_DEEPLINK to isNoteDeeplink
                )
            }
        }

        fun ReadHadithFragment.getHadith(): Hadith? {
            return arguments?.getParcelable(ARG_HADITH)
        }

        fun ReadHadithFragment.getSubtitle(): String? {
            return arguments?.getString(ARG_SUBTITLE)
        }

        fun ReadHadithFragment.getStartIndex(): Int? {
            return arguments?.getInt(ARG_START_INDEX, 1)
        }

        fun ReadHadithFragment.getEndIndex(): Int? {
            return arguments?.getInt(ARG_END_INDEX, getHadith()?.count ?: 0)
        }

        fun ReadHadithFragment.isNoteDeeplink(): Boolean {
            return arguments?.getBoolean(ARG_IS_NOTE_DEEPLINK, false) ?: false
        }
    }

    var pageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewDataBinding?.slider?.value =
                    viewModel.hadith.value?.get(position)?.index?.toFloat() ?: 1f
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.navigator = this
        viewModel.startIndex.value = getStartIndex()
        viewModel.endIndex.value = getEndIndex()
        viewModel.hadithId.value = getHadith()?.id
        hadithReadAdapterPaged = HadithReadAdapter()
        hadithReadAdapterVertical = HadithReadAdapter()

        reloadViews()
        observeData()
    }

    override fun reloadViews() {
        viewDataBinding?.recycler?.isVisible =
            Prefs.defaultHadithReadMode == ReadModeConstants.VERTICAL
        viewDataBinding?.pager?.isVisible = Prefs.defaultHadithReadMode == ReadModeConstants.PAGED
        viewDataBinding?.sliderContainer?.isVisible =
            Prefs.defaultHadithReadMode == ReadModeConstants.PAGED

        viewDataBinding?.titleText?.text = getHadith()?.name
        viewDataBinding?.subtitleText?.text = getSubtitle()
        viewDataBinding?.subtitleText?.isVisible = getSubtitle()?.isBlank() == false
        viewDataBinding?.chooseHadith?.isVisible = isNoteDeeplink()
        viewDataBinding?.chooseHadith?.onClickAnimate {
            val hadithIndex = viewModel.hadith.value?.get(viewDataBinding?.pager?.currentItem ?: 0)?.index ?: 1
            RxBus.getDefault().send(NoteInsertEvent("[${getHadith()?.name} ${hadithIndex}](https://dzikirqu.com/${getHadith()?.id}/${hadithIndex})"))
            requireActivity().finish()
        }
        viewDataBinding?.recycler?.apply {
            layoutManager = linearLayoutManager
            adapter = hadithReadAdapterVertical
        }
        viewDataBinding?.pager?.apply {
            adapter = hadithReadAdapterPaged.apply {
                isPager = true
            }
        }
        viewDataBinding?.settings?.setOnClickListener {
            SettingsBSD(arrayListOf(SettingsConstants.FONTS, SettingsConstants.HADITH)).show(
                childFragmentManager,
                ""
            )
        }

        viewDataBinding?.slider?.addOnChangeListener { slider, value, fromUser ->
            viewModel.hadith.value?.let {
                linearLayoutManager.scrollToPosition(it.indexOfFirst { it.index.toFloat() == value })
                viewDataBinding?.pager?.setCurrentItem(
                    it.indexOfFirst { it.index.toFloat() == value },
                    true
                )
            }
        }
    }

    fun observeData() {
        viewModel.hadith.observe(viewLifecycleOwner) {
            viewDataBinding?.progress?.isVisible = false


            if (viewModel.startIndex.value != viewModel.endIndex.value) {
                viewDataBinding?.sliderContainer?.animToY(0f)
                viewDataBinding?.sliderShadow?.animToY(0f)
                viewDataBinding?.slider?.valueFrom = it.first().index.toFloat()
                viewDataBinding?.slider?.valueTo = it.last().index.toFloat()
                viewDataBinding?.textValueFrom?.text = it.first().index
                viewDataBinding?.textValueTo?.text = it.last().index
                viewDataBinding?.pager?.registerOnPageChangeCallback(pageChangeCallback)
            } else {
                viewDataBinding?.sliderContainer?.isVisible = false
            }

            hadithReadAdapterVertical.submitList(it)
            hadithReadAdapterPaged.submitList(it)
        }
    }

}


