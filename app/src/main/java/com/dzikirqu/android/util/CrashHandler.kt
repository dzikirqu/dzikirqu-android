package com.dzikirqu.android.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import com.dzikirqu.android.ui.CrashActivity
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.StringBuilder

open class CrashHandler(private val appContext: Context) : Thread.UncaughtExceptionHandler {
    private val LINE_SEPARATOR = "\n"
    override fun uncaughtException(thread: Thread, exception: Throwable) {
        val stackTrace = StringWriter()
        exception.printStackTrace(PrintWriter(stackTrace))
        val errorReport = StringBuilder()
        errorReport.append("************ CAUSE OF ERROR ************\n\n")
        errorReport.append(stackTrace.toString())
        errorReport.append("\n************ DEVICE INFORMATION ***********\n")
        errorReport.append("Brand: ")
        errorReport.append(Build.BRAND)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("Device: ")
        errorReport.append(Build.DEVICE)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("Model: ")
        errorReport.append(Build.MODEL)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("Id: ")
        errorReport.append(Build.ID)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("Product: ")
        errorReport.append(Build.PRODUCT)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("\n************ FIRMWARE ************\n")
        errorReport.append("SDK: ")
        errorReport.append(Build.VERSION.SDK)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("Release: ")
        errorReport.append(Build.VERSION.RELEASE)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("Incremental: ")
        errorReport.append(Build.VERSION.INCREMENTAL)
        errorReport.append(LINE_SEPARATOR)

        val intent = Intent(appContext, CrashActivity::class.java)
        intent.putExtra("logs", errorReport.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        appContext.startActivity(intent)
        Process.killProcess(Process.myPid())
        System.exit(10)
    }
}