package com.wagyufari.dzikirqu.ui.bsd.notes.calligraphy

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.databinding.BsdComposeBinding
import com.wagyufari.dzikirqu.model.events.NoteInsertEvent
import com.wagyufari.dzikirqu.ui.v2.theme.uthman
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.horizontalSpacer
import com.wagyufari.dzikirqu.util.verticalSpacer


class NoteCalligraphyBSD : BottomSheetDialogFragment() {

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
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        horizontalSpacer(width = 16)
                        calligraphy(modifier = Modifier, "ﷺ")
                        calligraphy(modifier = Modifier, "ﷻ")
                        calligraphy(modifier = Modifier, "﷽")
                        calligraphy(modifier = Modifier, "رَحِمَهُ ٱللَّهُ")
                        calligraphy(modifier = Modifier, "عليه السلام")
                        calligraphy(modifier = Modifier, "رَضِيَ ٱللَّهُ عَنْهُ")
                        calligraphy(modifier = Modifier, "رَضِيَ ٱللَّهُ عَنْها")
                        calligraphy(modifier = Modifier, "حَفِظَهُ ٱللَّهُ")
                        horizontalSpacer(width = 16)
                    }
                }
                verticalSpacer(height = 16)
            }
        }
    }

    @Composable
    fun calligraphy(modifier: Modifier, text: String) {
        Text(
            modifier = modifier
                .padding(16.dp)
                .clickable {
                    RxBus
                        .getDefault()
                        .send(NoteInsertEvent(text))
                    dismiss()
                }, text = text, fontFamily = uthman, fontSize = 32.sp, color = colorResource(
                id = R.color.textTertiary
            )
        )
    }

}