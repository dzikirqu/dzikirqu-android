package com.dzikirqu.android.ui.bsd.notes.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.dzikirqu.android.R
import com.dzikirqu.android.databinding.BsdComposeBinding
import com.dzikirqu.android.model.events.NoteInsertEvent
import com.dzikirqu.android.ui.v2.theme.lato
import com.dzikirqu.android.util.RxBus
import com.dzikirqu.android.util.horizontalSpacer
import com.dzikirqu.android.util.verticalSpacer


class NoteHeaderBSD : BottomSheetDialogFragment() {

    lateinit var mBinding: BsdComposeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = BsdComposeBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.composeView.setContent {
            Column {
                verticalSpacer(height = 8)
                LazyRow(
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    item {
                        horizontalSpacer(width = 16)
                        calligraphy(modifier = Modifier, "H1", fontSize = 32f)
                        calligraphy(modifier = Modifier, "H2", fontSize = 24f)
                        calligraphy(modifier = Modifier, "H3", fontSize = 20.8f)
                        calligraphy(modifier = Modifier, "H4", fontSize = 16f)
                        calligraphy(modifier = Modifier, "H5", fontSize = 12.8f)
                        calligraphy(modifier = Modifier, "H6", fontSize = 11.2f)
                        horizontalSpacer(width = 16)
                    }
                }
                verticalSpacer(height = 16)
            }
        }
    }

    @Composable
    fun calligraphy(modifier: Modifier, text: String, fontSize:Float) {
        Text(
            modifier = modifier
                .padding(16.dp)
                .clickable {
                    when(text){
                        "H1"-> RxBus.getDefault().send(NoteInsertEvent("# "))
                        "H2"-> RxBus.getDefault().send(NoteInsertEvent("## "))
                        "H3"-> RxBus.getDefault().send(NoteInsertEvent("### "))
                        "H4"-> RxBus.getDefault().send(NoteInsertEvent("#### "))
                        "H5"-> RxBus.getDefault().send(NoteInsertEvent("##### "))
                        "H6"-> RxBus.getDefault().send(NoteInsertEvent("###### "))
                    }
                    dismiss()
                }, text = text, fontFamily = lato, fontSize = fontSize.sp, color = colorResource(
                id = R.color.textTertiary
            )
        )
    }

}