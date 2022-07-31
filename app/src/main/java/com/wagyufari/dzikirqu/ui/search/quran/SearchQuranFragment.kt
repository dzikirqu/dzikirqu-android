package com.wagyufari.dzikirqu.ui.search.quran

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.databinding.FragmentSearchQuranBinding
import com.wagyufari.dzikirqu.model.Ayah
import com.wagyufari.dzikirqu.ui.adapters.SearchQuranAdapter
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import com.wagyufari.dzikirqu.util.ViewUtils.isHidden
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchQuranFragment : BaseFragment<FragmentSearchQuranBinding, SearchQuranViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_search_quran
    override val viewModel: SearchQuranViewModel by viewModels()

    @Inject
    lateinit var searchQuranAdapter: SearchQuranAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewModel.result.observe(viewLifecycleOwner){
            viewDataBinding?.recycler?.isHidden(it.isEmpty())
            searchQuranAdapter.query = viewModel.query.value.toString()
            searchQuranAdapter.submitList(it)
            searchQuranAdapter.setListener(object:SearchQuranAdapter.Callback{
                override fun onSelectedItem(ayah: Ayah) {
                    requireActivity().finish()
                    ReadActivity.startSurah(requireActivity(), surah_id = ayah.chapterId, ayah = ayah.verse_number)
                }
            })
        }
        viewDataBinding?.recycler?.apply {
            adapter = searchQuranAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }
}