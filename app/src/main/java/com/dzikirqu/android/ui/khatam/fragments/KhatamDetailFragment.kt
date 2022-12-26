package com.dzikirqu.android.ui.khatam.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.databinding.FragmentKhatamDetailBinding
import com.dzikirqu.android.model.Khatam
import com.dzikirqu.android.model.QuranLastRead
import com.dzikirqu.android.ui.adapters.KhatamIterationAdapter
import com.dzikirqu.android.ui.bsd.StartReadQuranBSD
import com.dzikirqu.android.ui.khatam.presenter.setUpKhatam
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class KhatamDetailFragment : BaseFragment<FragmentKhatamDetailBinding, KhatamDetailViewModel>(),
    KhatamDetailNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_khatam_detail
    override val viewModel: KhatamDetailViewModel by viewModels()

    @Inject
    lateinit var khatamIterationAdapter: KhatamIterationAdapter

    companion object {

        const val ARG_KHATAM = "arg_khatam"

        fun newInstance(khatam: Khatam): KhatamDetailFragment {
            return KhatamDetailFragment().apply {
                arguments = bundleOf(ARG_KHATAM to khatam)
            }
        }

        fun KhatamDetailFragment.getKhatam(): Khatam? {
            return arguments?.getParcelable(ARG_KHATAM)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        getKhatam()?.let { setUpKhatam(it) }
        setUpRecyclerView()
    }


    fun openLastRead(lastRead: QuranLastRead) {
        StartReadQuranBSD.newInstantInstance(surah = lastRead.surah, ayah = lastRead.ayah).show(childFragmentManager, "")
    }

    fun setUpRecyclerView() {
        viewDataBinding?.rvIteration?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = khatamIterationAdapter
        }
    }

}