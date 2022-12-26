package com.dzikirqu.android.ui.main.prayer.fragments.grid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.databinding.FragmentPrayerGridBinding
import com.dzikirqu.android.model.events.PrayerTypeEvent
import com.dzikirqu.android.ui.adapters.PrayerTypeGridAdapter
import com.dzikirqu.android.ui.main.prayer.PrayerType
import com.dzikirqu.android.util.RxBus
import com.dzikirqu.android.util.prayerTypes
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PrayerGridFragment : BaseFragment<FragmentPrayerGridBinding, PrayerGridViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_prayer_grid
    override val viewModel: PrayerGridViewModel by viewModels()

    companion object {
        fun newInstance(): PrayerGridFragment {
            return PrayerGridFragment()
        }
    }

    @Inject
    lateinit var prayerTypeGridAdapter: PrayerTypeGridAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewDataBinding?.recycler?.apply {
            layoutManager = GridLayoutManager(requireActivity(), 4)
            adapter = prayerTypeGridAdapter
            prayerTypeGridAdapter.submitList(prayerTypes)
            prayerTypeGridAdapter.setListener(object:PrayerTypeGridAdapter.Callback{
                override fun onSelectedItem(type: PrayerType) {
                    RxBus.getDefault().send(PrayerTypeEvent(type))
                }
            })
        }

    }

}