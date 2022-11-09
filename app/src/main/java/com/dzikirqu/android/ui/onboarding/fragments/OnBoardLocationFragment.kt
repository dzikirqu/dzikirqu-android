package com.dzikirqu.android.ui.onboarding.fragments

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dzikirqu.android.databinding.FragmentOnBoardLocationBinding
import com.dzikirqu.android.model.events.OnBoardEvent
import com.dzikirqu.android.model.events.OnBoardType
import com.dzikirqu.android.ui.onboarding.fragments.viewmodels.OnBoardLoadingViewModel
import com.dzikirqu.android.util.RxBus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardLocationFragment : Fragment() {

    lateinit var binding: FragmentOnBoardLocationBinding

    val viewModel: OnBoardLoadingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardLocationBinding.inflate(
            LayoutInflater.from(requireActivity()),
            container,
            false
        )
        return binding.root
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true){
                RxBus.getDefault().send(OnBoardEvent(OnBoardType.PRAYTIME))
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.grant.setOnClickListener {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
        }
        binding.skip.setOnClickListener {
            startActivity(Intent(requireActivity(),OnBoardLoadingActivity::class.java))
            requireActivity().finish()
        }
    }
}

