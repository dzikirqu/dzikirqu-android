package com.dzikirqu.android.ui.bsd.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.dzikirqu.android.databinding.SettingsLayoutBinding
import com.dzikirqu.android.ui.bsd.settings.SettingsConstants.FONTS
import com.dzikirqu.android.ui.bsd.settings.SettingsConstants.GENERAL
import com.dzikirqu.android.ui.bsd.settings.SettingsConstants.HADITH
import com.dzikirqu.android.ui.bsd.settings.SettingsConstants.NOTES
import com.dzikirqu.android.ui.bsd.settings.SettingsConstants.PRAYTIME
import com.dzikirqu.android.ui.bsd.settings.SettingsConstants.QURAN
import com.dzikirqu.android.ui.bsd.settings.presenter.*
import com.dzikirqu.android.util.BooleanUtil.isXiaomi


class SettingsBSD(val settings: ArrayList<Int>? = null) : BottomSheetDialogFragment() {

    private lateinit var viewDataBinding: SettingsLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding =
            SettingsLayoutBinding.inflate(LayoutInflater.from(context), container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SettingsPresenter(requireActivity(), viewDataBinding).build(settings)
    }
}

class SettingsFragment(val settings: ArrayList<Int>? = null) : Fragment() {

    private lateinit var viewDataBinding: SettingsLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding =
            SettingsLayoutBinding.inflate(LayoutInflater.from(context), container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SettingsPresenter(requireActivity(), viewDataBinding).build(settings)
    }
}


class SettingsPresenter(val mContext: Context, val viewDataBinding: SettingsLayoutBinding) {

    fun build(settings: ArrayList<Int>?) {
        settings?.let {
            configureSettings(it)
        } ?: kotlin.run {
            configureSettings(
                arrayListOf(
                    GENERAL,
                    FONTS,
                    QURAN,
                    PRAYTIME,
                    HADITH,
                )
            )
        }
    }

    private fun configureSettings(settings: ArrayList<Int>) {
        configureViews()
        configureVisibility(settings)
    }

    fun configureViews() {
        language()
        text()
        theme()
        quran()
        calculationMethod()
        asrJuristic()
        higherAltitudes()
        muadzin()
        praytimeAdjustments()
        autostart()
        hadith()
        note()
    }

    private fun configureVisibility(settings: ArrayList<Int>) {
        viewDataBinding.textLanguage.isVisible = settings.contains(GENERAL)
        viewDataBinding.textGeneral.isVisible = settings.contains(GENERAL)
        viewDataBinding.textAppTheme.isVisible = settings.contains(GENERAL)
        viewDataBinding.dividerGeneral.isVisible = settings.contains(GENERAL)

        viewDataBinding.textTextSettings.isVisible = settings.contains(FONTS)
        viewDataBinding.arabicTitle.isVisible = settings.contains(FONTS)
        viewDataBinding.arabicSlider.isVisible = settings.contains(FONTS)
        viewDataBinding.translationTitle.isVisible = settings.contains(FONTS)
        viewDataBinding.arabicCheckbox.isVisible = settings.contains(FONTS)
        viewDataBinding.textArabicFont.isVisible = settings.contains(FONTS)
        viewDataBinding.translationSlider.isVisible = settings.contains(FONTS)
        viewDataBinding.translationCheckbox.isVisible = settings.contains(FONTS)
        viewDataBinding.dividerTextSettings.isVisible =
            (settings.contains(FONTS) && settings.contains(HADITH)) || (settings.contains(FONTS) && settings.contains(
                QURAN
            ))

        viewDataBinding.textQuranSettings.isVisible = settings.contains(QURAN)
        viewDataBinding.markLastReadAutoCheckbox.isVisible = settings.contains(QURAN)
        viewDataBinding.dividerQuranSettings.isVisible =
            settings.contains(QURAN) && settings.contains(PRAYTIME)
        viewDataBinding.textQuranReadMode.isVisible = settings.contains(QURAN)
        viewDataBinding.wbwCheckbox.isVisible = settings.contains(QURAN)

        viewDataBinding.textPraytimeSettings.isVisible = settings.contains(PRAYTIME)
        viewDataBinding.textCalculationMethod.isVisible = settings.contains(PRAYTIME)
        viewDataBinding.textAsrJuristic.isVisible = settings.contains(PRAYTIME)
        viewDataBinding.textHigherAltitudes.isVisible = settings.contains(PRAYTIME)
        viewDataBinding.textMuadzin.isVisible = settings.contains(PRAYTIME)
        viewDataBinding.textPraytimeAdjustments.isVisible = settings.contains(PRAYTIME)
        viewDataBinding.textAutostart.isVisible = settings.contains(PRAYTIME) && isXiaomi()

        viewDataBinding.textHadithSettings.isVisible = settings.contains(HADITH)
        viewDataBinding.textHadithReadMode.isVisible = settings.contains(HADITH)
        viewDataBinding.dividerHadithSettings.isVisible =
            settings.contains(HADITH) && settings.contains(QURAN)

        viewDataBinding.dividerNotesSettings.isVisible = settings.contains(PRAYTIME) && settings.contains(
            NOTES)
        viewDataBinding.notesFontSizeTitle.isVisible = settings.contains(NOTES)
        viewDataBinding.notesFontSizeSlider.isVisible = settings.contains(NOTES)
        viewDataBinding.notesWysiwyg.isVisible = settings.contains(NOTES)
    }
}

object SettingsConstants {
    const val GENERAL = 0
    const val FONTS = 1
    const val QURAN = 2
    const val HADITH = 3
    const val PRAYTIME = 4
    const val NOTES = 5
}