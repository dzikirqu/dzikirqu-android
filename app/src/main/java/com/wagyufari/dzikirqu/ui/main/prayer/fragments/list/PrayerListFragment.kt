package com.wagyufari.dzikirqu.ui.main.prayer.fragments.list

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
import com.wagyufari.dzikirqu.data.room.BookmarkDatabase
import com.wagyufari.dzikirqu.data.room.dao.getPrayerDao
import com.wagyufari.dzikirqu.databinding.FragmentPrayerListBinding
import com.wagyufari.dzikirqu.databinding.ItemPrayerBinding
import com.wagyufari.dzikirqu.model.Prayer
import com.wagyufari.dzikirqu.ui.adapters.GenericAdapter
import com.wagyufari.dzikirqu.ui.main.prayer.PrayerType
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import com.wagyufari.dzikirqu.util.StringExt.getText
import com.wagyufari.dzikirqu.util.binding.ImageViewBinding.setTintColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PrayerListFragment : BaseFragment<FragmentPrayerListBinding, PrayerListViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_prayer_list
    override val viewModel: PrayerListViewModel by viewModels()

    companion object {
        const val ARG_PRAYER_TYPE = "arg_prayer_type"
        fun newInstance(prayerType: PrayerType): PrayerListFragment {
            return PrayerListFragment().apply {
                arguments = bundleOf(ARG_PRAYER_TYPE to prayerType)
            }
        }

        fun PrayerListFragment.getPrayerType(): PrayerType {
            return arguments?.getParcelable(ARG_PRAYER_TYPE) ?: PrayerType(title = arrayListOf(),
                ids = listOf(), image = 0)
        }
    }

    lateinit var genericAdapter: GenericAdapter<Prayer, ItemPrayerBinding>

    @Inject
    lateinit var bookmarkDatabase:BookmarkDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        val mBookmarkDao = bookmarkDatabase.bookmarkDao()

        genericAdapter =
            GenericAdapter(ItemPrayerBinding::inflate, mListener2 = { prayer, mBinding, position ->
                mBinding.index.text = "${position + 1}. "
                mBinding.name.text = prayer.title?.getText()
                mBookmarkDao.getBookmarkById(prayer.id).observe(viewLifecycleOwner){
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

        viewDataBinding?.recycler?.apply {
            adapter = genericAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        lifecycleScope.launch {
            val prayers = if (getPrayerType().ids.isEmpty()) arrayListOf<Prayer>().apply {
                addAll(requireActivity().getPrayerDao().getPrayerByBookIdSuspend("1"))
                addAll(requireActivity().getPrayerDao().getPrayerByBookIdSuspend("0"))
                addAll(requireActivity().getPrayerDao().getPrayerByBookIdSuspend("2"))
            } else requireActivity().getPrayerDao().getPrayerByIdsSuspend(getPrayerType().ids.map { it.toString() })

            genericAdapter.submitList(if (getPrayerType().ids.isEmpty()) prayers else getPrayerType().ids.map { id ->
                prayers.firstOrNull { it.id == id.toString() }
            })
        }

    }

}