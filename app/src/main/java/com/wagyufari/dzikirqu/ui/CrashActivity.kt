package com.wagyufari.dzikirqu.ui

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.wagyufari.dzikirqu.data.Prefs

class CrashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Prefs.colorTheme)
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); // make a dialog without a titlebar
        setFinishOnTouchOutside(false); // prevent users from dismissing the dialog by tapping outside

        val logs = intent.getStringExtra("logs");
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Report Error!")
        alertDialog.setMessage(logs)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Okay") { dialog, id ->
            alertDialog.dismiss()
            finish()
        }
        alertDialog.show()

    }
}