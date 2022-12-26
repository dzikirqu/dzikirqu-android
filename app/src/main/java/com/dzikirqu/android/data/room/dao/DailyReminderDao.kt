package com.dzikirqu.android.data.room.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dzikirqu.android.data.room.GeneralDatabase
import com.dzikirqu.android.model.DailyReminder
import com.dzikirqu.android.model.DailyReminderParent

@Dao
interface DailyReminderDao {

    @Query("SELECT * FROM dailyReminder")
    suspend fun getDailyReminderSuspend():List<DailyReminder>
    @Query("SELECT * FROM dailyReminder")
     fun getDailyReminder():LiveData<List<DailyReminder>>
    @Query("SELECT * FROM dailyReminder WHERE parentId == :parentId")
     fun getDailyReminderByParentId(parentId:Int):LiveData<List<DailyReminder>>

    @Insert
    fun putDailyReminder(reminder:DailyReminder)

    @Update
    fun updateDailyReminder(reminder:DailyReminder)

    @Query("Delete from dailyReminder")
    fun deleteAllReminder()
}

fun Context.getDailyReminderDao(): DailyReminderDao {
    return GeneralDatabase.getDatabase(this).dailyReminderDao()
}
@Dao
interface DailyReminderParentDao {

    @Query("SELECT * FROM dailyReminderParent")
    suspend fun getDailyReminderSuspend():List<DailyReminderParent>
    @Query("SELECT * FROM dailyReminderParent")
     fun getDailyReminder():LiveData<List<DailyReminderParent>>

    @Insert
    fun putDailyReminder(reminder:DailyReminderParent):Long

    @Update
    fun updateDailyReminder(reminder:DailyReminderParent)

    @Query("Delete from dailyReminderParent")
    fun deleteAllReminder()
}

fun Context.getDailyReminderParentDao(): DailyReminderParentDao {
    return GeneralDatabase.getDatabase(this).dailyReminderParentDao()
}