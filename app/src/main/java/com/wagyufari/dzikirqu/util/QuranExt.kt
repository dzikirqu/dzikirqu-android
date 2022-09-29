package com.wagyufari.dzikirqu.util

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.room.PersistenceDatabase
import com.wagyufari.dzikirqu.model.QuranLastRead
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import com.wagyufari.dzikirqu.util.ViewUtils.getProgressValueContentView
import com.wagyufari.dzikirqu.util.ViewUtils.setMatchParentWidth


fun Context.configureInstallation(onInstalled: () -> Unit) {
    showDownloadConfirmationDialog {
        val splitInstallManager = SplitInstallManagerFactory.create(applicationContext);
        val request = SplitInstallRequest.newBuilder()
            .addModule("pagedquran")
            .build()
        val progressDialog = ViewUtils.getProgressValueDialog(this)
        progressDialog.setContentView(getProgressValueContentView(LocaleConstants.DOWNLOADING_FONTS.locale(), 0,1))
        splitInstallManager.registerListener {
            when (it.status()) {
                SplitInstallSessionStatus.DOWNLOADING->{
                    progressDialog.setContentView(getProgressValueContentView(LocaleConstants.DOWNLOADING_FONTS.locale(), it.bytesDownloaded(), it.totalBytesToDownload()))
                }
                SplitInstallSessionStatus.FAILED -> {
                    Toast.makeText(
                        this,
                        "Something went wrong, please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    progressDialog.dismiss()
                    onInstalled()
                }
            }
        }
        progressDialog.show()
        progressDialog.setMatchParentWidth()
        splitInstallManager.startInstall(request)
    }
}


fun Context.showDownloadConfirmationDialog(onPositive: () -> Unit) {
    AlertDialog.Builder(this)
        .setTitle(LocaleConstants.ADDITIONAL_DATA_REQUIRED.locale())
        .setMessage(LocaleConstants.TO_ACCESS_THE_PAGED_QURAN_FEATURE_AN_EXTRA_50_FILES_NEEDS_TO_BE_DOWNLOADED.locale())
        .setPositiveButton(LocaleConstants.DOWNLOAD.locale()) { dialog, which ->
            onPositive.invoke()
        }
        .setNegativeButton(LocaleConstants.CANCEL.locale()) { dialog, which ->
            dialog.dismiss()
        }.show()
}

fun Context.openPagedQuran(lastRead: QuranLastRead) {
    openPagedQuran(lastRead.surah, lastRead.ayah, lastRead.page)
}

fun Context.openPagedQuran(surah: Int, ayah: Int? = 1, page: Int? = null) {
//    val splitInstallManager = SplitInstallManagerFactory.create(this)
//    if (splitInstallManager.installedModules.contains("pagedquran").not()) {
//        this.configureInstallation {
//            openPagedQuran(surah, ayah, page)
//        }
//    } else {
        io {
            page?.let {
                ReadActivity.startPage(this, it)
            } ?: kotlin.run {
                val ayahLineDao = PersistenceDatabase.getDatabase(this).ayahLineDao()
                ayahLineDao.getAyahLineByKey("${surah}:${ayah ?: 1}")
                    .firstOrNull()?.let {
                        ReadActivity.startPage(this, it.page ?: 1)
                    } ?: kotlin.run {
                    ReadActivity.startPage(
                        this,
                        surah
                    )
                }
            }
        }
//    }
}