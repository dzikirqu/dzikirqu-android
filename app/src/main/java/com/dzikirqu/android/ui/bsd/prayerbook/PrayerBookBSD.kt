package com.dzikirqu.android.ui.bsd.prayerbook

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseDialog
import com.dzikirqu.android.databinding.PrayerBookBsdBinding
import com.dzikirqu.android.model.PrayerBook
import com.dzikirqu.android.ui.adapters.PrayerBookAdapter
import com.dzikirqu.android.ui.bsd.prayer.PrayerBSD
import com.dzikirqu.android.ui.bsd.prayer.PrayerBSDNavigator
import com.dzikirqu.android.ui.search.SearchActivity
import com.dzikirqu.android.ui.search.SearchTypeConstant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PrayerBookBSD : BaseDialog<PrayerBookBsdBinding, PrayerBookBSDViewModel>(),
    PrayerBSDNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.prayer_book_bsd
    override val viewModel: PrayerBookBSDViewModel by viewModels()

    @Inject
    lateinit var prayerBookAdapter: PrayerBookAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.navigator = this
        setUpViews()
    }

    fun setUpViews() {
        viewDataBinding?.search?.setOnClickListener {
            val intent = Intent(requireActivity(), SearchActivity::class.java).apply {
                putExtra(SearchActivity.EXTRA_SEARCH_TYPE, SearchTypeConstant.PRAYER)
                putExtra(SearchActivity.EXTRA_IS_NOTE_DEEPLINK, isNoteDeeplink())
            }
            val options = ActivityOptions.makeSceneTransitionAnimation(
                requireActivity(),
                viewDataBinding?.search,
                "shared_element_container"
            )
            startActivity(intent, options.toBundle())
            dismiss()
        }

        viewDataBinding?.recycler?.apply {
            adapter = prayerBookAdapter.apply {
                setListener(object:PrayerBookAdapter.Callback{
                    override fun onSelectedItem(book: PrayerBook) {
                        PrayerBSD.newInstance(book, isNoteDeeplink()).show(requireActivity().supportFragmentManager, "")
                        dismiss()
                    }
                })
            }
            layoutManager = linearLayoutManager
        }
        viewModel.books?.observe(viewLifecycleOwner){
            prayerBookAdapter.submitList(it)
        }
    }

    companion object{

        const val ARG_IS_NOTE_DEEPLINK = "arg_is_note_deeplink"

        @JvmStatic
        fun newInstance(isNoteDeeplink:Boolean?=null): PrayerBookBSD {
            return PrayerBookBSD().apply {
                arguments = bundleOf(ARG_IS_NOTE_DEEPLINK to isNoteDeeplink)
            }
        }

        fun PrayerBookBSD.isNoteDeeplink():Boolean{
            return arguments?.getBoolean(ARG_IS_NOTE_DEEPLINK, false) ?: false
        }

    }

}