package com.dzikirqu.android.ui.bsd.quran.deeplink

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFullDialog
import com.dzikirqu.android.databinding.ActivityQuranDeeplinkBinding
import com.dzikirqu.android.model.Surah
import com.dzikirqu.android.ui.adapters.SurahAdapter
import com.dzikirqu.android.ui.jump.JumpQuranActivity
import com.dzikirqu.android.ui.read.ReadActivity
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