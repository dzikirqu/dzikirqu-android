package com.wagyufari.dzikirqu.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wagyufari.dzikirqu.data.room.dao.DailyReminderDao
import com.wagyufari.dzikirqu.data.room.dao.DailyReminderParentDao
import com.wagyufari.dzikirqu.model.DailyReminder
import com.wagyufari.dzikirqu.model.DailyReminderParent
import com.wagyufari.dzikirqu.model.typeconverters.StringArrayConverter

@Database(
    entities = [DailyReminder::class, DailyReminderParent::class],
    version = 4
)
@TypeConverters(StringArrayConverter::class)
abstract class GeneralDatabase : RoomDatabase() {

    abstract fun dailyReminderDao(): DailyReminderDao
    abstract fun dailyReminderParentDao(): DailyReminderParentDao

    companion object {
        @Volatile
        private var instance: GeneralDatabase? = null

        fun getDatabase(context: Context): GeneralDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, GeneralDatabase::class.java, "general")
                .fallbackToDestructiveMigration()
                .build()
    }
}