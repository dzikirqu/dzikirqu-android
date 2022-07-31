package com.wagyufari.dzikirqu.ui.bsd.khatam

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseDialog
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.room.dao.getSurahDao
import com.wagyufari.dzikirqu.databinding.BsdKhatamFooterBinding
import com.wagyufari.dzikirqu.ui.khatam.presenter.setUpKhatam
import com.wagyufari.dzikirqu.util.openPagedQuran
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