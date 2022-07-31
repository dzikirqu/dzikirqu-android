package com.wagyufari.dzikirqu.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wagyufari.dzikirqu.data.room.dao.KhatamDao
import com.wagyufari.dzikirqu.model.Khatam
import com.wagyufari.dzikirqu.model.typeconverters.DateConverter
import com.wagyufari.dzikirqu.model.typeconverters.ListQuranLastReadConverter
import com.wagyufari.dzikirqu.model.typeconverters.QuranLastReadConverter
import com.wagyufari.dzikirqu.model.typeconverters.StringArrayConverter

@Database(
    entities = [Khatam::class],
    version = 2
)
@TypeConverters(StringArrayConverter::class, DateConverter::class, QuranLastReadConverter::class, ListQuranLastReadConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun khatamDao(): KhatamDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "database")
                .fallbackToDestructiveMigration()
                .build()
    }
}