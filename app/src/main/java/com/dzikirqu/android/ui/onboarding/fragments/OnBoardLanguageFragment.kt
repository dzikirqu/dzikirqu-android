package com.dzikirqu.android.ui.onboarding.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.FragmentOnBoardLanguageBinding
import com.dzikirqu.android.model.events.OnBoardEvent
import com.dzikirqu.android.model.events.OnBoardType
import com.dzikirqu.android.util.RxBus

class OnBoardLanguageFragment:Fragment(){

    lateinit var binding: FragmentOnBoardLanguageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentOnBoardLanguageBinding.inflate(LayoutInflater.from(requireActivity()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.english.setOnClickListener {
            Prefs.language = "english"
            RxBus.getDefault().send(OnBoardEvent(OnBoardType.LOCATION))
        }
        binding.bahasa.setOnClickListener {
            Prefs.language = "bahasa"
            RxBus.getDefault().send(OnBoardEvent(OnBoardType.LOCATION))
        }
    }
    
}

