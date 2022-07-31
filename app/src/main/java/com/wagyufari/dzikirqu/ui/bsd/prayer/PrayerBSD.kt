package com.wagyufari.dzikirqu.ui.bsd.prayer

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFullDialog
import com.wagyufari.dzikirqu.databinding.PrayerBsdBinding
import com.wagyufari.dzikirqu.model.Prayer
import com.wagyufari.dzikirqu.model.PrayerBook
import com.wagyufari.dzikirqu.model.events.NoteInsertEvent
import com.wagyufari.dzikirqu.ui.adapters.PrayerAdapter
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.StringExt.getText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PrayerBSD : BaseFullDialog<PrayerBsdBinding, PrayerBSDViewModel>(),
    PrayerBSDNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.prayer_bsd
    override val viewModel: PrayerBSDViewModel by viewModels()

    @Inject
    lateinit var prayerAdapter: PrayerAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var book:PrayerBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            book = it.getParcelable(ARG_BOOK)!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.navigator = this
        viewModel.book.set(book)
        setUpPrayer()
    }

    fun setUpPrayer() {
        viewDataBinding?.rvPrayer?.apply {
            adapter = prayerAdapter
            layoutManager = linearLayoutManager
            prayerAdapter.setListener(object : PrayerAdapter.Callback {
                override fun onSelectedItem(prayer: Prayer) {
                    if (isNoteDeeplink()){
                        RxBus.getDefault().send(NoteInsertEvent("[${prayer.title?.getText()}](https://dzikirqu.com/prayer/${prayer.id}/)"))
                        dismiss()
                    } else{
                        ReadActivity.startPrayer(requireActivity(), prayer)
                    }
                }
            })
        }
        viewModel.prayer(book.id).observe(viewLifecycleOwner) {
            prayerAdapter.submitList(it)
        }
        viewModel.dataManager.mBookmarkDatabase.bookmarkDao().getBookmarks()
            .observe(viewLifecycleOwner) {
                prayerAdapter.notifyDataSetChanged()
            }
    }

    companion object{

        const val ARG_BOOK = "arg_flyer"
        const val ARG_IS_NOTE_DEEPLINK = "arg_is_note_deeplink"

        @JvmStatic
        fun newInstance(book: PrayerBook, isNoteDeeplink:Boolean?=null): PrayerBSD {
            return PrayerBSD().apply {
                arguments = bundleOf(ARG_BOOK to book, ARG_IS_NOTE_DEEPLINK to isNoteDeeplink)
            }
        }

        fun PrayerBSD.isNoteDeeplink():Boolean{
            return arguments?.getBoolean(ARG_IS_NOTE_DEEPLINK, false) ?: false
        }

    }

}