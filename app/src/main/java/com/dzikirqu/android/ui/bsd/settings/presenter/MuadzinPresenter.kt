package com.dzikirqu.android.ui.bsd.settings.presenter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dzikirqu.android.R
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.ItemSettingsSoundBinding
import com.dzikirqu.android.ui.bsd.settings.SettingsPresenter

fun SettingsPresenter.muadzin(){
    viewDataBinding.textMuadzin.setOnClickListener {
        showMuadzin()
    }
    viewDataBinding.textMuadzin.setSubtitle(when (Prefs.muadzin) {
        R.raw.mansour_zahrany -> muadzinTitle[0]
        R.raw.ali_ahmed_mullah -> muadzinTitle[1]
        R.raw.ali_ahmed_mullah_2 -> muadzinTitle[2]
        R.raw.abdullah_al_zaili -> muadzinTitle[3]
        R.raw.khoirul_asfa -> muadzinTitle[4]
        R.raw.nasir_al_qatami -> muadzinTitle[5]
        else -> ""
    })
}

val muadzinTitle = arrayOf(
    "Mansour Al Zahrani",
    "Ali Ahmed Mullah (1)",
    "Ali Ahmed Mullah (2)",
    "Abdullah Al Zaili",
    "Khoirul Asfa (Rodja)",
    "Nasir Al-Qatami",
)
fun SettingsPresenter.showMuadzin() {
    val prefs = Prefs
    val muadzin = arrayOf(
        R.raw.mansour_zahrany,
        R.raw.ali_ahmed_mullah,
        R.raw.ali_ahmed_mullah_2,
        R.raw.abdullah_al_zaili,
        R.raw.khoirul_asfa,
        R.raw.nasir_al_qatami,
    )
    val mp = MediaPlayer()
    mp.setAudioStreamType(AudioManager.STREAM_ALARM)
    AlertDialog.Builder(mContext)
        .setTitle(LocaleConstants.MUADZIN.locale())
        .setAdapter(object :
            ArrayAdapter<Int>(mContext, R.layout.item_settings_sound, muadzin) {
            @SuppressLint("ViewHolder")
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return ItemSettingsSoundBinding.inflate(LayoutInflater.from(mContext)).apply {
                    name.text = muadzinTitle[position]
                    audioButton.setOnClickListener {
                        mp.stop()
                        mp.reset()
                        mp.setDataSource(
                            mContext,
                            Uri.parse("android.resource://" + mContext.packageName + "/" + muadzin[position])
                        )
                        mp.prepare()
                        mp.start()
                    }
                    mp.setOnCompletionListener {
                        mp.stop()
                        mp.reset()
                    }
                }.root
            }
        }) { dialog, which ->
            prefs.muadzin = muadzin[which]
            configureViews()
        }.setOnDismissListener {
            mp.stop()
            mp.reset()
        }
        .show()
}