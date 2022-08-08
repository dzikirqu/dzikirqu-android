package com.wagyufari.dzikirqu.ui.share

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.slider.Slider
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.databinding.ActivityShareImageBinding
import com.wagyufari.dzikirqu.util.BooleanUtil.isStorageGranted
import com.wagyufari.dzikirqu.util.ViewUtils
import com.wagyufari.dzikirqu.util.saveImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareImageActivity : AppCompatActivity() {

    lateinit var binding:ActivityShareImageBinding

    val requestStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
                shareImage()
            }
        }

    companion object{

        const val EXTRA_ARABIC = "extra_arabic"
        const val EXTRA_TRANSLATION = "extra_translation"
        const val EXTRA_SOURCE = "extra_source"
        const val EXTRA_TITLE = "extra_title"

        fun start(context: Context, arabic:String, translation:String, source:String, title:String?=null){
            context.startActivity(Intent(context, ShareImageActivity::class.java).apply {
                putExtra(EXTRA_ARABIC, arabic)
                putExtra(EXTRA_TRANSLATION, translation)
                putExtra(EXTRA_SOURCE, source)
                putExtra(EXTRA_TITLE, title)
            })
        }

        fun ShareImageActivity.getArabic():String{
            return intent.getStringExtra(EXTRA_ARABIC).toString()
        }
        fun ShareImageActivity.getTranslation():String{
            return intent.getStringExtra(EXTRA_TRANSLATION).toString()
        }
        fun ShareImageActivity.getSource():String{
            return intent.getStringExtra(EXTRA_SOURCE).toString()
        }
        fun ShareImageActivity.getTitleValue():String?{
            return intent.getStringExtra(EXTRA_TITLE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareImageBinding.inflate(LayoutInflater.from(this))
        binding.lifecycleOwner = this
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = LocaleConstants.SHARE.locale()
        }

        binding.arabic.text = getArabic()
        binding.translation.text = getTranslation()
        binding.source.text = getSource()
        binding.shareButton.text = LocaleConstants.SHARE.locale()
        binding.shareButton.setOnClickListener {
            if (isStorageGranted()) {
                shareImage()
            } else {
                requestStoragePermission.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }
        }

        binding.titleText.text = getTitleValue()
        binding.titleText.isVisible = getTitleValue() != null
        binding.title.isVisible = getTitleValue() != null
        binding.titleTitle.isVisible = getTitleValue() != null

        binding.arabicSwitch.setOnCheckedChangeListener { compoundButton, b ->
            binding.arabic.isVisible = b
        }
        binding.translationSwitch.setOnCheckedChangeListener { compoundButton, b ->
            binding.translation.isVisible = b
        }
        binding.titleSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            binding.titleText.textSize = value
        })
        binding.arabicSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            binding.arabic.textSize = value
        })
        binding.translationSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            binding.translation.textSize = value
        })
    }

    fun shareImage(){
        saveImage(
            ViewUtils.getBitmapFromView(
                binding.image
            )
        ) { uri ->
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, uri)
            }, LocaleConstants.SHARE.locale()))
        }
    }

}