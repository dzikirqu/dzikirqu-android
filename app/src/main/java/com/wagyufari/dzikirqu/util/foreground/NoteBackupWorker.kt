package com.wagyufari.dzikirqu.util.foreground

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.constants.NotificationConstants
import com.wagyufari.dzikirqu.data.ApiService
import com.wagyufari.dzikirqu.data.room.dao.getNoteDao
import com.wagyufari.dzikirqu.model.Note
import com.wagyufari.dzikirqu.model.request.toRequestModel
import com.wagyufari.dzikirqu.util.networkCall
import kotlinx.coroutines.tasks.await


class NoteBackupWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    val mChannelId = "com.wagyufari.dzikirqu.backup"
    private var notificationManager: NotificationManager? = null

    companion object{
        const val EXTRA_NOTE = "extra_note"

        fun start(context:Context, note:Note?=null){
            WorkManager.getInstance(context)
                .enqueue(
                    OneTimeWorkRequestBuilder<NoteBackupWorker>()
                        .setConstraints(
                            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()
                        )
                        .setInputData(workDataOf(EXTRA_NOTE to if (note != null) Gson().toJson(note) else null))
                        .build()
                )
        }

        fun NoteBackupWorker.getNote():Note?{
            return Gson().fromJson(inputData.getString(EXTRA_NOTE), Note::class.java)
        }
    }

    override suspend fun doWork(): Result {
        getNote()?.let {
            networkCall({ApiService.create().syncNote(it.toRequestModel())})
        }?: kotlin.run {
            val notes = applicationContext.getNoteDao().getNotesSuspended()
            if (notes.isEmpty() || Firebase.auth.currentUser == null) return Result.success()
            networkCall({ ApiService.create().syncNotes(notes.map { it.toRequestModel() }) })
            FirebaseStorage.getInstance().reference.child("notes/${Firebase.auth.currentUser?.uid}.json").putBytes(Gson().toJson(notes).toByteArray(Charsets.UTF_8)).await()
        }

        return Result.success()
    }

//    suspend fun syncSharedReference(note:Note){
//        if (note.shareId?.isBlank() == false) {
//            Firebase.storage.reference.child("notes/shared/${note.shareId}.json").putBytes(Gson().toJson(note.apply {
//                author = Author(
//                    uid = Firebase.auth.currentUser?.uid.toString(),
//                    name = Firebase.auth.currentUser?.displayName.toString(),
//                    profilePicture = Firebase.auth.currentUser?.photoUrl.toString()
//                )
//            }).toByteArray(Charsets.UTF_8)).await()
//        }
//    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(mChannelId, NotificationConstants.mChannelNameAdzan, importance)
        notificationManager?.createNotificationChannel(mChannel)
    }

    private fun Context.createForegroundNotification(text: String):ForegroundInfo{
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        val title = "Backup started"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        WorkManager.getInstance(applicationContext)

        val notification = NotificationCompat.Builder(applicationContext, mChannelId)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setDeleteIntent(WorkManager.getInstance(applicationContext).createCancelPendingIntent(id))
            .setSmallIcon(R.drawable.ic_dzikir)
            .setColor(applicationContext.resources.getColor(R.color.colorPrimary))
            .build()

        return  ForegroundInfo(NotificationConstants.mBackupId, notification)
    }
}