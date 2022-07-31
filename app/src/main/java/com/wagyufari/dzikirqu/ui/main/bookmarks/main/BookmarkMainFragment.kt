package com.wagyufari.dzikirqu.ui.main.bookmarks.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.databinding.FragmentMainBookmarkBinding
import com.wagyufari.dzikirqu.databinding.TabTextCircleBinding
import com.wagyufari.dzikirqu.model.BookmarkType
import com.wagyufari.dzikirqu.ui.adapters.FragmentPagerAdapter
import com.wagyufari.dzikirqu.ui.main.bookmarks.prayer.BookmarkPageFragment
import com.wagyufari.dzikirqu.util.Appbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookmarkMainFragment : BaseFragment<FragmentMainBookmarkBinding, BookmarkMainViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_main_bookmark
    override val viewModel: BookmarkMainViewModel by viewModels()

    companion object{

        const val ARG_TYPE = "arg_type"

        fun start(context:Context){
            context.startActivity(Intent(context, BookmarkMainFragment::class.java))
        }

        fun BookmarkMainFragment.getType():String{
            return requireActivity().intent.getStringExtra("extra_type") ?: BookmarkType.QURAN
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.let { configureViews(it) }
    }

    fun configureViews(viewDataBinding: FragmentMainBookmarkBinding){
        viewDataBinding.lifecycleOwner = this

        viewDataBinding.appbar.setContent {
            Appbar().setTitle("Bookmarks", fontSize = 20).withBackButton().build()
        }

        val fragments = arrayListOf(BookmarkPageFragment.newInstance(BookmarkType.QURAN),BookmarkPageFragment.newInstance(BookmarkType.PRAYER))
        val titles = arrayListOf(LocaleConstants.QURAN.locale(), LocaleConstants.PRAYER.locale())

        viewDataBinding.pager.apply {
            adapter = FragmentPagerAdapter(
                requireActivity() as AppCompatActivity,
                if (getType() == BookmarkType.QURAN) fragments else fragments.reversed()
            )
        }
        TabLayoutMediator(viewDataBinding.tab, viewDataBinding.pager) { tab, position ->
            tab.customView = TabTextCircleBinding.inflate(LayoutInflater.from(requireActivity())).apply{
                title.text = if (getType() == BookmarkType.QURAN) titles[position] else titles.reversed()[position]
            }.root
        }.attach()
    }
}