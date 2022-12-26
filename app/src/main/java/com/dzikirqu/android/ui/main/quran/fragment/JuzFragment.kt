package com.dzikirqu.android.ui.main.quran.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.databinding.FragmentJuzBinding
import com.dzikirqu.android.model.Hizb
import com.dzikirqu.android.model.Juz
import com.dzikirqu.android.ui.adapters.JuzAdapter
import com.dzikirqu.android.ui.read.ReadActivity
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
