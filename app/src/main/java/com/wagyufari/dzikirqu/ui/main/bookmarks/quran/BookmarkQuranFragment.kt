package com.wagyufari.dzikirqu.ui.main.bookmarks.quran

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.data.room.dao.getAyahDao
import com.wagyufari.dzikirqu.data.room.dao.getSurahDao
import com.wagyufari.dzikirqu.databinding.FragmentBookmarkQuranBinding
import com.wagyufari.dzikirqu.databinding.ItemBookmarkQuranBinding
import com.wagyufari.dzikirqu.model.Bookmark
import com.wagyufari.dzikirqu.ui.adapters.GenericAdapter
import com.wagyufari.dzikirqu.util.LocaleProvider
import com.wagyufari.dzikirqu.util.binding.ImageViewBinding.setTintColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BookmarkQuranFragment : BaseFragment<FragmentBookmarkQuranBinding, BookmarkQuranViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_bookmark_quran
    override val viewModel: BookmarkQuranViewModel by viewModels()

    lateinit var genericAdapter: GenericAdapter<Bookmark, ItemBookmarkQuranBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        genericAdapter = GenericAdapter(ItemBookmarkQuranBinding::inflate, mListener1 = { bookmark, mBinding->

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

                }
            }
        })

        viewDataBinding?.recycler?.apply {
            adapter = genericAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }

        viewModel.bookmarks.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                genericAdapter.submitList(it)
            }
        }
    }
}