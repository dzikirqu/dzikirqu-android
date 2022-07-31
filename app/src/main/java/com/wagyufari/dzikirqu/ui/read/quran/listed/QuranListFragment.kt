package com.wagyufari.dzikirqu.ui.read.quran.listed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.dao.getKhatamDao
import com.wagyufari.dzikirqu.data.room.dao.getQuranLastReadDao
import com.wagyufari.dzikirqu.data.room.dao.getSurahDao
import com.wagyufari.dzikirqu.databinding.FragmentQuranListBinding
import com.wagyufari.dzikirqu.model.Ayah
import com.wagyufari.dzikirqu.model.KhatamStateConstants
import com.wagyufari.dzikirqu.model.QuranLastRead
import com.wagyufari.dzikirqu.model.events.CurrentAyahEvent
import com.wagyufari.dzikirqu.model.events.MenuEvent
import com.wagyufari.dzikirqu.model.events.NoteInsertEvent
import com.wagyufari.dzikirqu.model.update
import com.wagyufari.dzikirqu.ui.adapters.QuranListedAdapter
import com.wagyufari.dzikirqu.ui.bsd.quranmenu.QuranMenuBSD
import com.wagyufari.dzikirqu.ui.read.quran.AyahQuery
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.io
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class QuranListFragment :
    BaseFragment<FragmentQuranListBinding, QuranListViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_quran_list
    override val viewModel: QuranListViewModel by viewModels()

    var quranListedAdapter = QuranListedAdapter()
    var query: AyahQuery? = null
    var ayah: Int? = null
    var currentLastRead: QuranLastRead? = null

    companion object {

        const val ARG_QUERY = "arg_query"
        const val ARG_AYAH = "arg_ayah"
        const val ARG_IS_NOTE_DEEPLINK = "arg_is_note_deeplink"

        fun newInstance(query: AyahQuery, ayah: Int, isNoteDeeplink:Boolean?=false): QuranListFragment {
            return QuranListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_QUERY, query)
                    putInt(ARG_AYAH, ayah)
                    putBoolean(ARG_IS_NOTE_DEEPLINK, isNoteDeeplink ?: false)
                }
            }
        }

        fun QuranListFragment.isNoteDeeplink():Boolean{
            return arguments?.getBoolean(ARG_IS_NOTE_DEEPLINK, false) ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            query = it.getParcelable(ARG_QUERY)
            ayah = it.getInt(ARG_AYAH)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpQuran()
    }


    override fun onResume() {
        super.onResume()
        viewDataBinding?.recycler?.scrollBy(0, 1)
    }

    fun setUpQuran() {
        viewDataBinding?.recycler?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = quranListedAdapter
        }

        viewModel.getAyah(query!!).observe(viewLifecycleOwner){ data->
            quranListedAdapter.apply {
                addItems(data.toCollection(arrayListOf()))

                viewDataBinding?.recycler?.scrollToPosition(positionOfAyah(ayah!!))

                setListener(object : QuranListedAdapter.Callback {
                    override fun onSelectedItem(ayah: Ayah) {
                        if (isNoteDeeplink()){
                            io {
                                val surah = getSurahDao().getSurahById(ayah.chapterId).firstOrNull()
                                RxBus.getDefault().send(NoteInsertEvent("[${surah?.name} ${ayah.verse_number}](https://dzikirqu.com/quran/${surah?.id}/${ayah.verse_number})"))
                                requireActivity().finish()
                            }
                        } else{
                            QuranMenuBSD.newInstance(ayah.chapterId, ayah.verse_number).show(
                                childFragmentManager,
                                ""
                            )
                        }

                    }
                })
                viewDataBinding?.recycler?.addOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
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

        viewModel.dataManager.mBookmarkDatabase.bookmarkDao().getBookmarks()
            .observe(viewLifecycleOwner) {
                quranListedAdapter.notifyDataSetChanged()
            }
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

    fun getAyahFromPosition(data: ArrayList<Any>): Ayah? {
        val position =
            (viewDataBinding?.recycler?.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        val ayah = data[if (position == -1) 0 else position]
        if (ayah is Ayah) {
            return ayah
        }
        return null
    }
}