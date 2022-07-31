package com.wagyufari.dzikirqu.ui.khatam.composer

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseDialog
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.dao.getKhatamDao
import com.wagyufari.dzikirqu.databinding.KhatamComposerBsdBinding
import com.wagyufari.dzikirqu.model.*
import com.wagyufari.dzikirqu.ui.bsd.settings.presenter.notificationSound
import com.wagyufari.dzikirqu.util.io
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class KhatamComposerBSD : BaseDialog<KhatamComposerBsdBinding, KhatamComposerViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.khatam_composer_bsd
    override val viewModel: KhatamComposerViewModel by viewModels()

    companion object {

        const val ARG_KHATAM = "arg_khatam"

        fun newInstance(khatam: Khatam): KhatamComposerBSD {
            return KhatamComposerBSD().apply {
                arguments = bundleOf(ARG_KHATAM to khatam)
            }
        }

        fun KhatamComposerBSD.getKhatam(): Khatam? {
            return arguments?.getParcelable(ARG_KHATAM)
        }

        fun KhatamComposerBSD.getKhatamCount(): String {
            return viewDataBinding?.khatamCount?.editText?.text?.toString() ?: "Khattam 1x"
        }

        fun KhatamComposerBSD.getReminder(): String {
            return viewDataBinding?.reminder?.editText?.text?.toString()
                ?: LocaleConstants.EVERY_FARDH_PRAYER.locale()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        getKhatam()?.let {
            setUpEdit(it)
        } ?: kotlin.run {
            setUpCreate()
        }

        setUpViews()
    }

    fun setUpCreate() {
        viewDataBinding?.save?.setOnClickListener {
            createNewKhatam()
        }
    }

    fun setUpEdit(khatam: Khatam) {
        viewModel.khatam.value = khatam
        viewDataBinding?.khatamCount?.editText?.setText("Khatam ${khatam.iteration?.count()}x")
        viewDataBinding?.reminder?.editText?.setText(Prefs.khatamReminderType.toStringLocale())


        viewDataBinding?.save?.setOnClickListener {
            editKhatam()
        }
        setUpViews()
    }

    fun setUpViews() {
        viewDataBinding?.khatamCount?.hint = LocaleConstants.NUMBER_OF_KHATAM.locale()
        (viewDataBinding?.khatamCount?.editText as? AutoCompleteTextView)?.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.list_item,
                listOf(
                    "Khattam 1x",
                    "Khattam 2x",
                    "Khattam 3x",
                    "Khattam 4x",
                    "Khattam 5x",
                    "Khattam 6x"
                )
            )
        )

        viewDataBinding?.reminder?.hint = LocaleConstants.REMINDER.locale()
        (viewDataBinding?.reminder?.editText as? AutoCompleteTextView)?.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.list_item,
                listOf(
                    LocaleConstants.EVERY_1_HOUR.locale(),
                    LocaleConstants.EVERY_2_HOUR.locale(),
                    LocaleConstants.EVERY_3_HOUR.locale(),
                    LocaleConstants.EVERY_4_HOUR.locale(),
                    LocaleConstants.EVERY_FARDH_PRAYER.locale()
                )
            )
        )

        viewDataBinding?.notification?.hint = LocaleConstants.NOTIFICATION_SOUND.locale()
        notificationSound()

    }

    fun editKhatam() {
        val currentCount = viewModel.khatam.value?.iteration?.count() ?: 1
        val editedCount =
            viewDataBinding?.khatamCount?.editText?.text?.toString()?.replace(Regex("[^0-9]"), "")
                ?.toInt() ?: 1
        if (currentCount > editedCount) {
            viewModel.khatam.value?.iteration = viewModel.khatam.value?.iteration?.filter { it.lap <= editedCount }
        } else {
            while (viewModel.khatam.value?.iteration?.count() ?: 1 < editedCount) {
                viewModel.addNewKhatam()
            }
        }
        io {
            Prefs.khatamReminderType = getReminder().toQuranReminderNotificationType()
            viewModel.khatam.value?.let { getKhatamDao().updateKhatam(it) }
            dismiss()
        }
    }

    fun createNewKhatam() {
        io {
            val iterations = arrayListOf<QuranLastRead>()
            val khatamCountString = viewDataBinding?.khatamCount?.editText?.text?.toString()
            val khatamCount = if (khatamCountString?.isNullOrBlank() == true) 1 else khatamCountString.replace(Regex("[^0-9]"), "")
                .toInt()
            for (i in 1..khatamCount) {
                iterations.add(
                    QuranLastRead(
                        surah = 1,
                        ayah = 1,
                        page = null,
                        isSavedFromPage = null,
                        timestamp = null
                    ).apply {
                        lap = i
                        state =
                            if (i == 1) KhatamStateConstants.ACTIVE else KhatamStateConstants.INACTIVE
                    }
                )
            }
            Prefs.khatamReminderType = getReminder().toQuranReminderNotificationType()
            Khatam.schedule(requireActivity())
            getKhatamDao().putKhatam(
                Khatam(
                    name = "",
                    startDate = Calendar.getInstance().time,
                    endDate = Calendar.getInstance().apply { add(Calendar.MONTH, 1) }.time,
                    state = KhatamStateConstants.ACTIVE,
                    iteration = iterations
                )
            )
            dismiss()
        }
    }
}