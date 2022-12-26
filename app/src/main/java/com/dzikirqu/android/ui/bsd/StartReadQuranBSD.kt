package com.dzikirqu.android.ui.bsd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.constants.ReadModeConstants
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.BsdComposeBinding
import com.dzikirqu.android.ui.read.ReadActivity
import com.dzikirqu.android.ui.v2.theme.lato
import com.dzikirqu.android.util.horizontalSpacer
import com.dzikirqu.android.util.openPagedQuran
import com.dzikirqu.android.util.thumb
import com.dzikirqu.android.util.verticalSpacer
import com.dzikirqu.android.R

class StartReadQuranBSD : BottomSheetDialogFragment() {

    lateinit var mBinding: BsdComposeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = BsdComposeBinding.inflate(inflater)
        return mBinding.root
    }

    companion object {
        const val ARG_SURAH = "arg_surah"
        const val ARG_AYAH = "arg_ayah"
        const val ARG_PAGE = "arg_page"
        const val ARG_INSTANT = "arg_instant"

        fun newInstantInstance(
            surah: Int? = null,
            ayah: Int? = null,
            page: Int? = null,
        ): StartReadQuranBSD {
            return StartReadQuranBSD().apply {
                arguments = bundleOf(ARG_SURAH to surah,
                    ARG_AYAH to ayah,
                    ARG_PAGE to page,
                    ARG_INSTANT to true)
            }
        }

        fun newSelectionInstance(
            surah: Int? = null,
            ayah: Int? = null,
            page: Int? = null,
        ): StartReadQuranBSD {
            return StartReadQuranBSD().apply {
                arguments = bundleOf(ARG_SURAH to surah,
                    ARG_AYAH to ayah,
                    ARG_PAGE to page,
                    ARG_INSTANT to false)
            }
        }

        fun StartReadQuranBSD.getSurah(): Int? {
            return arguments?.getInt(ARG_SURAH)
        }

        fun StartReadQuranBSD.isInstant(): Boolean {
            return arguments?.getBoolean(ARG_INSTANT) ?: false
        }

        fun StartReadQuranBSD.getAyah(): Int? {
            return arguments?.getInt(ARG_AYAH)
        }

        fun StartReadQuranBSD.getPage(): Int? {
            return arguments?.getInt(ARG_PAGE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isInstant()) {
            start()
        } else{
            compose()
        }
    }

    fun start() {
        when (Prefs.defaultQuranReadMode) {
            ReadModeConstants.PAGED -> {
                getSurah()?.let { requireActivity().openPagedQuran(surah = it, ayah = getAyah()) }
                dismiss()
            }
            else->{
                getSurah()?.let { ReadActivity.startSurah(requireActivity(), it, getAyah()) }
                dismiss()
            }
        }
    }

    fun compose() {
        mBinding.composeView.setContent {
            val interactionSource = remember { MutableInteractionSource() }
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                verticalSpacer(height = 16)
                thumb()
                verticalSpacer(height = 32)
                Row(modifier = Modifier) {
                    Column(modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ){
                        Prefs.defaultQuranReadMode = ReadModeConstants.VERTICAL
                        start()
                    }, horizontalAlignment = Alignment.CenterHorizontally) {
                        Card(shape = RoundedCornerShape(8.dp),
                            backgroundColor = colorResource(id = R.color.neutral_0)) {
                            Image(modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .height(80.dp)
                                .width(80.dp),
                                painter = painterResource(id = R.drawable.quran_list),
                                contentDescription = null)
                        }
                        verticalSpacer(height = 8)
                        Text(text = LocaleConstants.VERTICAL.locale(),
                            fontFamily = lato,
                            fontSize = 16.sp,
                            color = colorResource(
                                id = R.color.textPrimary))
                        Text(text = LocaleConstants.READ_IN_VERTICAL_MODE.locale(),
                            fontFamily = lato,
                            fontSize = 12.sp,
                            color = colorResource(
                                id = R.color.textPrimary))
                    }
                    horizontalSpacer(width = 48.dp)
                    Column(modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ){
                        Prefs.defaultQuranReadMode = ReadModeConstants.PAGED
                        start()
                    }, horizontalAlignment = Alignment.CenterHorizontally) {
                        Card(shape = RoundedCornerShape(8.dp),
                            backgroundColor = colorResource(id = R.color.neutral_0)) {
                            Image(modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .height(80.dp)
                                .width(80.dp),
                                painter = painterResource(id = R.drawable.quran_paged),
                                contentDescription = null)
                        }
                        verticalSpacer(height = 8)
                        Text(text = LocaleConstants.PAGED.locale(),
                            fontFamily = lato,
                            fontSize = 16.sp,
                            color = colorResource(
                                id = R.color.textPrimary))
                        Text(text = LocaleConstants.READ_IN_PAGED_MODE.locale(),
                            fontFamily = lato,
                            fontSize = 12.sp,
                            color = colorResource(
                                id = R.color.textPrimary))
                    }
                }
                verticalSpacer(height = 32)
            }
        }
    }

}