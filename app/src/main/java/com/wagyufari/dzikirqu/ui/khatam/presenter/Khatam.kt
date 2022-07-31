package com.wagyufari.dzikirqu.ui.khatam.presenter

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.dao.getAyahLineDao
import com.wagyufari.dzikirqu.data.room.dao.getKhatamDao
import com.wagyufari.dzikirqu.data.room.dao.getSurahDao
import com.wagyufari.dzikirqu.model.*
import com.wagyufari.dzikirqu.ui.adapters.KhatamIterationAdapter
import com.wagyufari.dzikirqu.ui.bsd.khatam.KhatamFooterBSD
import com.wagyufari.dzikirqu.ui.khatam.composer.KhatamComposerBSD
import com.wagyufari.dzikirqu.ui.khatam.fragments.KhatamDetailFragment
import com.wagyufari.dzikirqu.ui.main.quran.MainQuranFragment
import com.wagyufari.dzikirqu.util.io
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.PeriodType
import org.joda.time.format.PeriodFormatterBuilder
import java.text.SimpleDateFormat
import java.util.*


fun MainQuranFragment.setUpActiveKhatam(khatam: Khatam?) {
//    val isKhatamNull = khatam == null
//    viewDataBinding?.textProgressKhatam?.isVisible = !isKhatamNull
//    viewDataBinding?.textProgressPercentageKhatam?.isVisible = !isKhatamNull
//    viewDataBinding?.progressKhatam?.isVisible = !isKhatamNull
//    viewDataBinding?.textSubtitleKhatam?.isVisible = isKhatamNull
//
//    val hijriMonth =
//        UmmalquraCalendar().apply { khatam?.startDate?.let { time = it } }.getDisplayName(
//            Calendar.MONTH,
//            Calendar.LONG,
//            Locale.ENGLISH
//        )
//    val hijriYear =
//        UmmalquraCalendar().apply { khatam?.startDate?.let { time = it } }.get(Calendar.YEAR)
//
//    viewDataBinding?.textTitleKhatam?.text =
//        String.format(LocaleConstants.KHATAM_TARGET_MONTH_N.locale(), hijriMonth) + " $hijriYear"
//    viewDataBinding?.textSubtitleKhatam?.text =
//        LocaleConstants.NO_ACTIVE_KHATAM_SESSION_YET.locale()
//
//    io {
//        val value = khatam?.iteration?.map {
//            getAyahLineDao().getAyahLineByKey("${it.surah}:${it.ayah}").firstOrNull()?.page
//                ?: 1
//        }?.sumOf { it }
//        val maxValue = 604 * (khatam?.iteration?.count() ?: 1)
//
//        main {
//            viewDataBinding?.progressKhatam?.max = maxValue
//            ValueAnimator.ofInt(0, value ?: 1).apply {
//                this.duration = 2000
//                addUpdateListener {
//                    val value = it.animatedValue as Int
//                    viewDataBinding?.progressKhatam?.progress = value
//                    viewDataBinding?.textProgressPercentageKhatam?.text = "${value?.times(100)?.div(maxValue)}%"
//                }
//                start()
//            }
//            viewDataBinding?.textProgressKhatam?.text =
//                LocaleConstants.PAGE.locale() + " " + String.format(
//                    LocaleConstants.OF.locale(),
//                    value,
//                    maxValue
//                )
//        }
//    }

}

fun KhatamFooterBSD.setUpKhatam() {
    var min = 0
    getKhatamDao().getKhatam().observe(viewLifecycleOwner) {
        io {
            it?.getOngoing()?.let { khatam ->
                val lastRead = khatam.iteration?.getOngoing()
                val value = khatam.iteration?.map {
                    getAyahLineDao().getAyahLineByKey("${it.surah}:${it.ayah}").firstOrNull()?.page
                        ?: 1
                }?.sumOf { it } ?: 1
                val maxValue = 604 * (khatam.iteration?.count() ?: 1)

                val daysInMonth = PeriodFormatterBuilder()
                    .appendDays().appendSuffix("", "")
                    .toFormatter().print(
                        Period(
                            DateTime(it.getOngoing()?.startDate),
                            DateTime(it.getOngoing()?.endDate),
                            PeriodType.dayTime()
                        )
                    )?.toInt() ?: 1

                val elapsedTimeString = PeriodFormatterBuilder()
                    .appendDays().appendSuffix("", "")
                    .toFormatter().print(
                        Period(
                            DateTime(it.getOngoing()?.startDate),
                            DateTime(Calendar.getInstance().time),
                            PeriodType.dayTime()
                        )
                    )

                val elapsedTime = if (elapsedTimeString.isNullOrBlank()) 0 else elapsedTimeString.toInt()

                val dailyTarget = maxValue / daysInMonth
                val targetYesteday = dailyTarget * (elapsedTime)
                val targetToday = dailyTarget * (elapsedTime + 1)

                if (min == 0 && (value - targetYesteday) < 0) {
                    min = (value - targetYesteday)
                }

                val todayValue = value - (targetYesteday + min)
                val todayMax = (targetToday - targetYesteday) + (min * -1)
                val unreadPages = (todayMax - dailyTarget)

                val surahName = lastRead?.surah?.let { it1 ->
                    getSurahDao().getSurahById(it1).firstOrNull()?.name
                }

                viewModel.textKhatamIndex.postValue("Khattam #${lastRead?.lap}")
                viewDataBinding?.khatamProgress?.max = todayMax
                viewDataBinding?.khatamProgress?.progress = todayValue
                viewModel.textKhatamProgressPercentage.postValue(
                    "${
                        todayValue.times(100).div(todayMax)
                    }%"
                )
                viewModel.textKhatamLastRead.postValue("$surahName ${lastRead?.ayah}")
                viewModel.textKhatamProgress.postValue(
                    if (unreadPages > 0)
                        String.format(
                            LocaleConstants.TODAY_TARGET_PAGE_N_OF_N_PLUS_N.locale(),
                            todayValue,
                            dailyTarget,
                            unreadPages
                        )
                    else
                        String.format(
                            LocaleConstants.TODAY_TARGET_PAGE_N_OF_N.locale(),
                            todayValue,
                            dailyTarget
                        )
                )
            }
        }
    }

    viewDataBinding?.checkBoxAutoLastRead?.isChecked = Prefs.isMarkQuranLastReadAutomatically
    viewDataBinding?.checkBoxAutoLastRead?.setOnCheckedChangeListener { compoundButton, b ->
        Prefs.isMarkQuranLastReadAutomatically = b
    }
}



@SuppressLint("UseCompatLoadingForColorStateLists")
fun KhatamDetailFragment.setUpKhatam(khatam: Khatam) {
    lifecycleScope.launch {
        khatamIterationAdapter.submitList(khatam.iteration)
        khatamIterationAdapter.notifyDataSetChanged()

        khatamIterationAdapter.isInactive(khatam.state != KhatamStateConstants.ACTIVE)
        khatamIterationAdapter.setListener(object : KhatamIterationAdapter.Callback {
            override fun onSelectIteration(lastRead: QuranLastRead) {
                lastRead.setAsActive(requireActivity(), khatam)
            }

            override fun onClickRead(lastRead: QuranLastRead) {
                openLastRead(lastRead)
            }
        })

        val hijriMonth =
            UmmalquraCalendar().apply { khatam.startDate?.let { time = it } }.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.ENGLISH
            )
        val hijriYear =
            UmmalquraCalendar().apply { khatam.startDate?.let { time = it } }.get(Calendar.YEAR)

        val isActive = khatam.state == KhatamStateConstants.ACTIVE
        viewDataBinding?.edit?.isVisible = isActive
        viewDataBinding?.textReminder?.isVisible = isActive
        viewDataBinding?.textKhatamCount?.isVisible = isActive

        viewDataBinding?.textTitle?.text = "Khattam $hijriMonth $hijriYear"
        viewDataBinding?.textKhatamCount?.text = "Khattam ${khatam.iteration?.count()}x"
        viewDataBinding?.textReminder?.text =
            "${LocaleConstants.REMIND.locale()} ${Prefs.khatamReminderType.toStringLocale()}"
        val value = khatam.iteration?.map {
            getAyahLineDao().getAyahLineByKey("${it.surah}:${it.ayah}").firstOrNull()?.page
                ?: 1
        }?.sumOf { it }
        val maxValue = 604 * (khatam.iteration?.count() ?: 1)

        viewDataBinding?.progress?.max = maxValue
        ValueAnimator.ofInt(0, value ?: 1).apply {
            this.duration = 2000
            addUpdateListener {
                val value = it.animatedValue as Int
                viewDataBinding?.progress?.progress = value
                viewDataBinding?.textProgressPercentage?.text =
                    "${value?.times(100)?.div(maxValue)}%"
            }
            start()
        }


        val startDate = DateTime(Calendar.getInstance().time)
        val endDate = DateTime(khatam.endDate);
        val period = Period(startDate, endDate, PeriodType.dayTime());
        val formatter = PeriodFormatterBuilder()
            .appendDays().appendSuffix("", "")
            .toFormatter()

        viewDataBinding?.timeContainer?.isVisible = isActive
        viewDataBinding?.textProgress?.text = LocaleConstants.PAGE.locale() + " " + String.format(
            LocaleConstants.OF.locale(),
            value,
            maxValue
        )
        viewDataBinding?.textDaysLeft?.text =
            formatter.print(period) + " ${LocaleConstants.DAYS_LEFT.locale()}"
        viewDataBinding?.textEndDate?.text =
            SimpleDateFormat("dd MMM yyyy").format(khatam.endDate)
        viewDataBinding?.edit?.setOnClickListener {
            KhatamComposerBSD.newInstance(khatam).show(childFragmentManager, "")
        }
    }
}