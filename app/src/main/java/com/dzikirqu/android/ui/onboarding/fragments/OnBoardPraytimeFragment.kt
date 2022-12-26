package com.dzikirqu.android.ui.onboarding.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dzikirqu.android.constants.RingType
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.FragmentOnBoardPraytimeBinding
import com.dzikirqu.android.ui.onboarding.fragments.viewmodels.OnBoardPraytimeViewModel

class OnBoardPraytimeFragment:Fragment(){

    lateinit var binding: FragmentOnBoardPraytimeBinding
    val viewModel: OnBoardPraytimeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardPraytimeBinding.inflate(LayoutInflater.from(requireActivity()), container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
    }

    fun configureViews(){
        binding.next.setOnClickListener {
            startActivity(Intent(requireActivity(),OnBoardLoadingActivity::class.java))
            requireActivity().finish()
        }
        binding.ringFajr.setOnClickListener {
            Prefs.ringFajr = nextRingValue(viewModel.ringFajr.get() ?: 0)
            viewModel.ringFajr.set(Prefs.ringFajr)
        }
        binding.ringDhuhr.setOnClickListener {
            Prefs.ringDhuhr = nextRingValue(viewModel.ringDhuhr.get() ?: 0)
            viewModel.ringDhuhr.set(Prefs.ringDhuhr)
        }
        binding.ringAsr.setOnClickListener {
            Prefs.ringAsr = nextRingValue(viewModel.ringAsr.get() ?: 0)
            viewModel.ringAsr.set(Prefs.ringAsr)
        }
        binding.ringMaghrib.setOnClickListener {
            Prefs.ringMaghrib = nextRingValue(viewModel.ringMaghrib.get() ?: 0)
            viewModel.ringMaghrib.set(Prefs.ringMaghrib)
        }
        binding.ringIsya.setOnClickListener {
            Prefs.ringIsya = nextRingValue(viewModel.ringIsya.get() ?: 0)
            viewModel.ringIsya.set(Prefs.ringIsya)
        }
    }

    fun nextRingValue(currentValue:Int):Int{
        return when(currentValue){
            RingType.SOUND -> RingType.NOTIFICATION
            RingType.NOTIFICATION -> RingType.SILENT
            else -> RingType.SOUND
        }
    }
    
}

