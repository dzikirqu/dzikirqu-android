package com.dzikirqu.android.ui.bsd.khatam

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseDialog
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.room.dao.getSurahDao
import com.dzikirqu.android.databinding.BsdKhatamFooterBinding
import com.dzikirqu.android.ui.khatam.presenter.setUpKhatam
import com.dzikirqu.android.util.openPagedQuran
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KhatamFooterBSD : BaseDialog<BsdKhatamFooterBinding, KhatamFooterViewModel>(), KhatamFooterNavigator{

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.bsd_khatam_footer
    override val viewModel: KhatamFooterViewModel by viewModels()

    companion object{
        const val ARG_SURAH = "arg_surah"
        const val ARG_AYAH = "arg_ayah"

        fun newInstance(surah:Int?, ayah:Int?):KhatamFooterBSD{
            return KhatamFooterBSD().apply {
                arguments = bundleOf(ARG_SURAH to surah, ARG_AYAH to ayah)
            }
        }

        fun KhatamFooterBSD.getAyah():Int{
            return arguments?.getInt(ARG_AYAH, 1) ?: 1
        }
        fun KhatamFooterBSD.getSurah():Int{
            return arguments?.getInt(ARG_SURAH, 1) ?: 1
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launch {
            getSurahDao().getSurahByIdSingle(getSurah())?.let { surah->
                viewModel.readCurrentAyahInPagedMode.postValue(String.format(LocaleConstants.READ_N_IN_PAGED_MODE.locale(),
                    "${surah.name} ${getAyah()}"))
            }
        }

        setUpKhatam()
    }

    override fun onClickPage() {
        requireActivity().openPagedQuran(getSurah(),
            getAyah())
    }

}