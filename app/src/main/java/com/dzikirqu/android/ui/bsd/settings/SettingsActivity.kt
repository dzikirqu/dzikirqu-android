package com.dzikirqu.android.ui.bsd.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.ActivitySettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    lateinit var binding:ActivitySettingsBinding

    companion object{
        fun start(mContext: Context){
            mContext.startActivity(Intent(mContext, SettingsActivity::class.java))
        }
        fun newIntent(mContext: Context):Intent{
            return Intent(mContext, SettingsActivity::class.java)
        }
        fun start(view: View){
            val mContext = view.context
            mContext.startActivity(Intent(mContext, SettingsActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Prefs.colorTheme)
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.text = LocaleConstants.SETTINGS.locale()
        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return super.onSupportNavigateUp()
    }
}