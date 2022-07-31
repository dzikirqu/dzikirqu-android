package com.wagyufari.dzikirqu.ui.bsd.quran.deeplink

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFullDialog
import com.wagyufari.dzikirqu.databinding.ActivityQuranDeeplinkBinding
import com.wagyufari.dzikirqu.model.Surah
import com.wagyufari.dzikirqu.ui.adapters.SurahAdapter
import com.wagyufari.dzikirqu.ui.jump.JumpQuranActivity
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuranDeeplinkBSD : BaseFullDialog<ActivityQuranDeeplinkBinding, QuranDeeplinkViewModel>(), QuranDeeplinkNavigator{

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_quran_deeplink
    override val viewModel: QuranDeeplinkViewModel by viewModels()

    @Inject
    lateinit var surahAdapter: SurahAdapter
    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewDataBinding?.jump?.setOnClickListener {
            JumpQuranActivity.start(requireActivity(),true)
        }

        viewDataBinding?.surah?.apply {
            layoutManager = linearLayoutManager
            adapter = surahAdapter
        }
        surahAdapter.setListener(object:SurahAdapter.Callback{
            override fun onClickSurah(surah: Surah) {
                ReadActivity.startSurah(requireActivity(), surah.id, 1, true)
            }

            override fun onLongClickSurah(surah: Surah) {

            }
        })
        viewModel.surah.observe(viewLifecycleOwner){
            surahAdapter.submitList(it)
        }
    }

    override fun onDeeplinkEvent() {
        dismiss()
    }

}