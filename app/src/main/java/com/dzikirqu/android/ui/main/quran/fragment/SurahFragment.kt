package com.dzikirqu.android.ui.main.quran.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.databinding.FragmentSurahBinding
import com.dzikirqu.android.model.Surah
import com.dzikirqu.android.ui.adapters.SurahAdapter
import com.dzikirqu.android.ui.bsd.StartReadQuranBSD
import com.dzikirqu.android.ui.read.ReadActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SurahFragment : BaseFragment<FragmentSurahBinding, SurahViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_surah
    override val viewModel: SurahViewModel by viewModels()

    @Inject
    lateinit var surahAdapter: SurahAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    companion object {
        const val ARG_IS_NOTE_DEEPLINK = "arg_is_note_deeplink"

        fun newInstance(isNoteDeeplink: Boolean): SurahFragment {
            return SurahFragment().apply {
                arguments = bundleOf(ARG_IS_NOTE_DEEPLINK to isNoteDeeplink)
            }
        }

        fun SurahFragment.isNoteDeeplink(): Boolean {
            return arguments?.getBoolean(ARG_IS_NOTE_DEEPLINK, false) ?: false
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewDataBinding?.recycler?.apply {
            layoutManager = linearLayoutManager
            adapter = surahAdapter
        }

        viewModel.surah.observe(requireActivity()) {
            viewModel.isLoading.value = false
            surahAdapter.submitList(it)
        }

        surahAdapter.setListener(object : SurahAdapter.Callback {
            override fun onClickSurah(surah: Surah) {

                if (isNoteDeeplink()){
                    ReadActivity.startSurah(requireActivity(), surah.id, 1, isNoteDeeplink())
                    return
                }


                StartReadQuranBSD.newInstantInstance(surah = surah.id, ayah = 1).show(childFragmentManager, "")
            }

            override fun onLongClickSurah(surah: Surah) {
                StartReadQuranBSD.newSelectionInstance(surah = surah.id, ayah = 1).show(childFragmentManager, "")
            }
        })
    }
}