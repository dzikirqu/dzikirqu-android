package com.dzikirqu.android.ui.bsd.notes.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.databinding.BsdComposeBinding
import com.dzikirqu.android.ui.bsd.hadithbook.HadithBookBSD
import com.dzikirqu.android.ui.bsd.prayerbook.PrayerBookBSD
import com.dzikirqu.android.ui.quran.MainQuranActivity
import com.dzikirqu.android.ui.v2.theme.lato
import com.dzikirqu.android.util.verticalSpacer
import com.dzikirqu.android.R

class NoteDeeplinkTypeBSD : BottomSheetDialogFragment() {

    lateinit var mBinding: BsdComposeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = BsdComposeBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.composeView.setContent {
            Column {
                verticalSpacer(height = 8)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                dismiss()
                                startActivity(MainQuranActivity.newIntent(requireActivity(), true))
                            },
                        LocaleConstants.QURAN.locale(),
                        R.drawable.ic_quran
                    )
                    card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                dismiss()
                                PrayerBookBSD
                                    .newInstance(true)
                                    .show(requireActivity().supportFragmentManager, "")
                            },
                        LocaleConstants.PRAYER.locale(),
                        R.drawable.ic_book
                    )
                    card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                dismiss()
                                HadithBookBSD
                                    .newInstance(true)
                                    .show(requireActivity().supportFragmentManager, "")
                            },
                        LocaleConstants.HADITH.locale(),
                        R.drawable.ic_collection_book
                    )
                }
                verticalSpacer(height = 16)
            }
        }
    }

    @Composable
    private fun card(modifier: Modifier, name: String, image: Int) {
        Card(
            modifier = modifier,
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
            backgroundColor = colorResource(id = R.color.neutral_50)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                verticalSpacer(height = 16)
                Image(
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp)
                        .padding(2.dp),
                    painter = painterResource(id = image),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colorResource(id = R.color.colorPrimary))
                )
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = name,
                    fontFamily = lato,
                    fontSize = 14.sp,
                    maxLines = 1,
                    color = colorResource(id = R.color.textPrimary)
                )
                verticalSpacer(height = 8)
            }
        }
    }

}