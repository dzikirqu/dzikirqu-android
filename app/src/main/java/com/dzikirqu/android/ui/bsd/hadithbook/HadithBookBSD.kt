package com.dzikirqu.android.ui.bsd.hadithbook

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseDialog
import com.dzikirqu.android.databinding.HadithBookBsdBinding
import com.dzikirqu.android.model.Hadith
import com.dzikirqu.android.ui.adapters.HadithBookAdapter
import com.dzikirqu.android.ui.hadithchapter.HadithChapterActivity
import com.dzikirqu.android.ui.read.ReadActivity
import com.dzikirqu.android.util.HadithUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HadithBookBSD : BaseDialog<HadithBookBsdBinding, HadithBookBSDViewModel>(),
    HadithBookBSDNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.hadith_book_bsd
    override val viewModel: HadithBookBSDViewModel by viewModels()

    @Inject
    lateinit var hadithBookAdapter: HadithBookAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.navigator = this
        setUpRecycler()
    }

    fun setUpRecycler() {
        viewDataBinding?.recycler?.apply {
            adapter = hadithBookAdapter.apply {
                setListener(object : HadithBookAdapter.Callback {
                    override fun onSelectedItem(hadith: Hadith, view: View) {
                        if (hadith.metadata != null) {
                            HadithChapterActivity.start(requireActivity(), hadith, isNoteDeeplink())
                        } else {
                            ReadActivity.startHadith(requireActivity(), hadith, isNoteDeeplink())
                        }
                        if (isNoteDeeplink()) dismiss()
                    }
                })
                submitList(HadithUtils.getHadiths())
            }
            layoutManager = linearLayoutManager
        }
    }

    companion object {

        const val ARG_IS_NOTE_DEEPLINK = "arg_is_note_deeplink"

        @JvmStatic
        fun newInstance(isNoteDeeplink:Boolean?=false): HadithBookBSD {
            return HadithBookBSD().apply{
                arguments = bundleOf(ARG_IS_NOTE_DEEPLINK to isNoteDeeplink)
            }
        }

        fun HadithBookBSD.isNoteDeeplink():Boolean{
            return arguments?.getBoolean(ARG_IS_NOTE_DEEPLINK) ?: false
        }

    }

}