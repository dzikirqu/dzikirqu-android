package com.dzikirqu.android.ui.main.bookmarks.quran

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.data.room.dao.getAyahDao
import com.dzikirqu.android.data.room.dao.getSurahDao
import com.dzikirqu.android.databinding.FragmentBookmarkQuranBinding
import com.dzikirqu.android.databinding.ItemBookmarkQuranBinding
import com.dzikirqu.android.model.Bookmark
import com.dzikirqu.android.ui.adapters.GenericAdapter
import com.dzikirqu.android.util.LocaleProvider
import com.dzikirqu.android.util.binding.ImageViewBinding.setTintColor
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