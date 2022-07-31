package com.wagyufari.dzikirqu.ui.bsd.settings.presenter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.databinding.ItemSettingsSoundBinding
import com.wagyufari.dzikirqu.model.Khatam
import com.wagyufari.dzikirqu.ui.khatam.composer.KhatamComposerBSD

fun KhatamComposerBSD.notificationSound(){
    viewDataBinding?.notification?.editText?.setOnClickListener {
            showQari()
    }

    viewDataBinding?.notificationSoundSwitch?.isChecked = Prefs.khatamReminderSound != -1
    viewDataBinding?.notificationSoundSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked){
            Prefs.khatamReminderSound = 0
        } else{
            Prefs.khatamReminderSound = -1
        }
        Khatam.schedule(requireActivity())
        setText()
    }
    setText()
}

private fun KhatamComposerBSD.setText(){
    viewDataBinding?.notification?.isVisible = Prefs.khatamReminderSound != -1
    viewDataBinding?.notification?.editText?.setText(when (Prefs.khatamReminderSound) {
        R.raw.bismillah_abdullahbasfar -> qariTitle[0]
        R.raw.bismillah_mishary -> qariTitle[1]
        R.raw.bismillah_sudais -> qariTitle[2]
        R.raw.bismillah_abdullahmatrood -> qariTitle[3]
        0 -> qariTitle[4]
        else -> "-"
    })
}

val qariTitle = arrayOf(
    "Abdullah Basfar",
    "Mishary Rashid Alafasy",
    "Abdul Rahman As-Sudais",
    "Abdullah Al Matrood",
    LocaleConstants.NO_SOUND.locale()
)
fun KhatamComposerBSD.showQari() {
    val prefs = Prefs
    val qari = arrayOf(
        R.raw.bismillah_abdullahbasfar,
        R.raw.bismillah_mishary,
        R.raw.bismillah_sudais,
        R.raw.bismillah_abdullahmatrood,
        0,
    )
    val mp = MediaPlayer()
    mp.setAudioStreamType(AudioManager.STREAM_MUSIC)
    AlertDialog.Builder(requireActivity())
        .setTitle(LocaleConstants.NOTIFICATION_SOUND.locale())
        .setAdapter(object :
            ArrayAdapter<Int>(requireActivity(), R.layout.item_settings_sound, qari) {
            @SuppressLint("ViewHolder")
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return ItemSettingsSoundBinding.inflate(LayoutInflater.from(requireActivity())).apply {
                    name.text = qariTitle[position]
                    audioButton.setOnClickListener {
                        mp.stop()
                        mp.reset()
                        mp.setDataSource(
                            requireActivity(),
                            Uri.parse("android.resource://" + requireActivity().packageName + "/" + qari[position])
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
            prefs.khatamReminderSound = qari[which]
            viewDataBinding?.notification?.editText?.setText(when (Prefs.khatamReminderSound) {
                R.raw.bismillah_abdullahbasfar -> qariTitle[0]
                R.raw.bismillah_mishary -> qariTitle[1]
                R.raw.bismillah_sudais -> qariTitle[2]
                R.raw.bismillah_abdullahmatrood -> qariTitle[3]
                0 -> qariTitle[4]
                else -> ""
            })
        }.setOnDismissListener {
            mp.stop()
            mp.reset()
        }
        .show()
}