package com.wagyufari.dzikirqu.ui.flyer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseActivity
import com.wagyufari.dzikirqu.databinding.ActivityFlyerBinding
import com.wagyufari.dzikirqu.model.Flyer
import com.wagyufari.dzikirqu.ui.adapters.FlyerLoadStateAdapter
import com.wagyufari.dzikirqu.ui.adapters.FlyerPagingAdapter
import com.wagyufari.dzikirqu.ui.bsd.flyer.FlyerBSD
import com.wagyufari.dzikirqu.util.binding.ImageViewBinding.setTintColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FlyerActivity : BaseActivity<ActivityFlyerBinding, FlyerViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_flyer
    override val viewModel: FlyerViewModel by viewModels()

    @Inject
    lateinit var flyerPagingAdapter: FlyerPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.lifecycleOwner = this

        viewDataBinding.recycler.apply {
            adapter = flyerPagingAdapter.apply {
                asGrid()
            }.withLoadStateFooter(FlyerLoadStateAdapter{
                flyerPagingAdapter.retry()
            })
            layoutManager = GridLayoutManager(this@FlyerActivity,1)
        }

//        lifecycleScope.launchWhenCreated {
//            viewModel.flyer.collectLatest {
//                flyerPagingAdapter.submitData(it)
//            }
//        }

        lifecycleScope.launch {
            flyerPagingAdapter.loadStateFlow.collectLatest {
                viewDataBinding.progress.isVisible = it.refresh is LoadState.Loading
            }
        }

        viewModel.grid.observe(this){
            when(it){
                1-> viewDataBinding.recycler.layoutManager = GridLayoutManager(this@FlyerActivity,1)
                2-> viewDataBinding.recycler.layoutManager = GridLayoutManager(this@FlyerActivity,2)
                3-> viewDataBinding.recycler.layoutManager = GridLayoutManager(this@FlyerActivity,3)
            }
            viewDataBinding.grid1.setTintColor(if (it == 1) R.color.colorPrimary else R.color.neutral_400)
            viewDataBinding.grid2.setTintColor(if (it == 2) R.color.colorPrimary else R.color.neutral_400)
            viewDataBinding.grid3.setTintColor(if (it == 3) R.color.colorPrimary else R.color.neutral_400)
        }

        flyerPagingAdapter.setListener(object : FlyerPagingAdapter.Callback {
            override fun onSelectedItem(flyer: Flyer) {
                FlyerBSD.newInstance(flyer).show(supportFragmentManager, "")
            }
        })

    }
}