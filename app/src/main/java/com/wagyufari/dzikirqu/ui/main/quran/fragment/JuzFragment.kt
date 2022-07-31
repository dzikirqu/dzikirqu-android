package com.wagyufari.dzikirqu.ui.main.quran.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.databinding.FragmentJuzBinding
import com.wagyufari.dzikirqu.model.Hizb
import com.wagyufari.dzikirqu.model.Juz
import com.wagyufari.dzikirqu.ui.adapters.JuzAdapter
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JuzFragment : BaseFragment<FragmentJuzBinding, JuzViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_juz
    override val viewModel: JuzViewModel by viewModels()

    @Inject
    lateinit var juzAdapter: JuzAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewDataBinding?.recycler?.apply {
            layoutManager = linearLayoutManager
            adapter = juzAdapter
        }


        viewModel.juz.observe(viewLifecycleOwner){
            juzAdapter.clearItems()
            juzAdapter.addItems(it.toCollection(arrayListOf()))
        }

        juzAdapter.setListener(object:JuzAdapter.Callback{
            override fun onSelectJuz(juz: Juz) {
                ReadActivity.startJuz(requireActivity(),juz)
            }
            override fun onSelectHizb(hizb: Hizb) {
                ReadActivity.startHizb(requireActivity(),hizb )
            }

            override fun onTapExpand(juz: Juz) {
                viewModel.isShowHizbOnJuz.value = juz.juz
            }
        })
    }

}
