package com.dzikirqu.android.ui.bsd.quran.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.dzikirqu.android.R
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.BsdComposeBinding
import com.dzikirqu.android.model.events.SettingsEvent
import com.dzikirqu.android.ui.v2.theme.lato
import com.dzikirqu.android.util.RxBus
import com.dzikirqu.android.util.thumb
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainQuranFilterBSD : BottomSheetDialogFragment() {

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
            var viewMode by remember { mutableStateOf(Prefs.quranFilterMode) }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                thumb()
                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorResource(id = R.color.neutral_100)),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    viewMode(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                Prefs.quranFilterMode = 0
                                RxBus
                                    .getDefault()
                                    .send(SettingsEvent())
                                viewMode = 0
                            }, "Surah", isSelected = viewMode == 0
                    )
                    viewMode(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                Prefs.quranFilterMode = 1
                                RxBus
                                    .getDefault()
                                    .send(SettingsEvent())
                                viewMode = 1
                            }, "Juz", isSelected = viewMode == 1
                    )
                }
                Spacer(Modifier.height(64.dp))
            }
        }
    }


    @Composable
    fun viewMode(modifier: Modifier, text: String, isSelected: Boolean) {
        Card(
            modifier = modifier.padding(if (isSelected) 8.dp else 0.dp),
            elevation = if (isSelected) 1.dp else 0.dp,
            backgroundColor = colorResource(id = if (isSelected) R.color.colorPrimary else R.color.neutral_100),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                )
                Text(text = text, fontFamily = lato, fontSize = 18.sp, color = colorResource(id = if (isSelected) R.color.white else R.color.textPrimary))
                Spacer(Modifier.height(12.dp))
            }
        }
    }

}