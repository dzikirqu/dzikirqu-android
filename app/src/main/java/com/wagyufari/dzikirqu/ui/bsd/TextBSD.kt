package com.wagyufari.dzikirqu.ui.bsd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.databinding.BsdTextBinding
import com.wagyufari.dzikirqu.ui.v2.theme.lato
import com.wagyufari.dzikirqu.util.thumb
import com.wagyufari.dzikirqu.util.verticalSpacer

class TextBSD : BottomSheetDialogFragment() {

    lateinit var mBinding: BsdTextBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = BsdTextBinding.inflate(inflater)
        return mBinding.root
    }

    companion object {
        const val ARG_TEXT = "arg_text"

        fun newInstance(text: String?): TextBSD {
            return TextBSD().apply {
                arguments = bundleOf(ARG_TEXT to text)
            }
        }

        fun TextBSD.getText(): String {
            return arguments?.getString(ARG_TEXT) ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.composeView.setContent {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                verticalSpacer(height = 16)
                thumb()
                verticalSpacer(height = 16)
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.neutral_200))
                    .fillMaxWidth(0.9f)){
                    Text(modifier = Modifier
                        .padding(16.dp),
                        text = getText(),
                        fontFamily = lato,
                        fontSize = 20.sp,
                        color = colorResource(
                            id = R.color.textTertiary))
                }
                verticalSpacer(height = 24)
            }
        }
    }

}