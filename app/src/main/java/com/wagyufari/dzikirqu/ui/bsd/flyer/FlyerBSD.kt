package com.wagyufari.dzikirqu.ui.bsd.flyer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFullDialog
import com.wagyufari.dzikirqu.databinding.FlyerBsdBinding
import com.wagyufari.dzikirqu.model.Flyer
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels

@AndroidEntryPoint
class FlyerBSD : BaseFullDialog<FlyerBsdBinding, FlyerBSDViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.flyer_bsd
    override val viewModel: FlyerBSDViewModel by viewModels()

    private var flyer: Flyer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flyer = it.getParcelable(ARG_FLYER)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.flyer.value = flyer

        isImmutable = true
        viewDataBinding?.image?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isImmutable = false
                }
                MotionEvent.ACTION_UP->{
                    isImmutable = true
                }
            }
            false
        }
    }

    companion object{

        const val ARG_FLYER = "arg_flyer"

        @JvmStatic
        fun newInstance(flyer:Flyer):FlyerBSD{
            return FlyerBSD().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FLYER, flyer)
                }
            }
        }
    }
}