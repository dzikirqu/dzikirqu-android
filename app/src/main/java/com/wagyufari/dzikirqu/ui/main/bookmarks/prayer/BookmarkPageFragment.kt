package com.wagyufari.dzikirqu.ui.main.bookmarks.prayer

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.data.room.dao.getAyahDao
import com.wagyufari.dzikirqu.data.room.dao.getBookmarkDao
import com.wagyufari.dzikirqu.data.room.dao.getPrayerDao
import com.wagyufari.dzikirqu.data.room.dao.getSurahDao
import com.wagyufari.dzikirqu.databinding.FragmentBookmarkPageBinding
import com.wagyufari.dzikirqu.databinding.ItemBookmarkQuranBinding
import com.wagyufari.dzikirqu.databinding.ItemPrayerBinding
import com.wagyufari.dzikirqu.model.Bookmark
import com.wagyufari.dzikirqu.model.BookmarkType
import com.wagyufari.dzikirqu.model.Prayer
import com.wagyufari.dzikirqu.ui.adapters.GenericAdapter
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import com.wagyufari.dzikirqu.util.LocaleProvider
import com.wagyufari.dzikirqu.util.StringExt.getText
import com.wagyufari.dzikirqu.util.binding.ImageViewBinding.setTintColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BookmarkPageFragment : BaseFragment<FragmentBookmarkPageBinding, BookmarkPageViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_bookmark_page
    override val viewModel: BookmarkPageViewModel by viewModels()

    lateinit var prayerAdapter: GenericAdapter<Prayer, ItemPrayerBinding>
    lateinit var quranAdapter: GenericAdapter<Bookmark, ItemBookmarkQuranBinding>
    lateinit var quranPage: GenericAdapter<Bookmark, ItemBookmarkQuranBinding>

    companion object{

        const val ARG_TYPE = "arg_type"

        fun newInstance(type:String):BookmarkPageFragment{
            return BookmarkPageFragment().apply {
                arguments = bundleOf(ARG_TYPE to type)
            }
        }

        fun BookmarkPageFragment.getType():String{
            return arguments?.getString(ARG_TYPE) ?: BookmarkType.QURAN
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        prayerAdapter = GenericAdapter(ItemPrayerBinding::inflate, mListener1 = { prayer, mBinding->
            mBinding.name.text = prayer.title?.getText()
            requireActivity().getBookmarkDao().getBookmarkById(prayer.id).observe(viewLifecycleOwner){
                it?.let { bookmark->
                    mBinding.bookmark.setTintColor(R.color.colorPrimary)
                    mBinding.bookmark.setOnClickListener {
                        viewModel.removeBookmark(prayer)
                    }
                    mBinding.highlight.isVisible = true
                    mBinding.highlight.setTintColor(if(bookmark.highlighted) R.color.colorPrimary else R.color.neutral_300)
                    mBinding.highlight.setOnClickListener {
                        viewModel.updateHighlight(bookmark)
                    }
                }?: kotlin.run {
                    mBinding.bookmark.setTintColor(R.color.neutral_300)
                    mBinding.bookmark.setOnClickListener {
                        viewModel.addBookmark(prayer)
                    }
                    mBinding.highlight.isVisible = false
                }
            }
            mBinding.root.setOnClickListener {
                ReadActivity.startPrayer(requireActivity(), prayer)
            }
        })

        quranAdapter = GenericAdapter(ItemBookmarkQuranBinding::inflate, mListener1 = { bookmark, mBinding->

            lifecycleScope.launch {
                val ayah = getAyahDao().getAyahById(bookmark.idString?.toInt() ?: 1)
                val surah = requireActivity().getSurahDao().getSurahById(ayah?.chapterId?: 1).firstOrNull()
                mBinding.surah.text = surah?.name
                mBinding.ayah.text = String.format(
                    LocaleProvider.getString(LocaleConstants.AYAH_N),
                    ayah?.verse_number
                )
                mBinding.bookmark.setTintColor(R.color.colorPrimary)
                mBinding.bookmark.setOnClickListener {
                    viewModel.removeBookmark(ayah?.id.toString())
                }
                mBinding.root.setOnClickListener {
                    ReadActivity.startSurah(requireActivity(), ayah?.chapterId, ayah?.verse_number)
                }
            }
        })

        viewDataBinding?.recycler?.apply {
            adapter = if (getType() == BookmarkType.QURAN) quranAdapter else prayerAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        viewModel.quran.observe(viewLifecycleOwner){
            quranAdapter.submitList(it)
        }

        viewModel.prayer.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                prayerAdapter.submitList(it.map {
                    requireActivity().getPrayerDao().getPrayerByIdSuspend(it.idString.toString())
                        .firstOrNull()
                })
            }
        }

    }
}