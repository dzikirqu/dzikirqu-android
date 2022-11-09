package com.dzikirqu.android.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dzikirqu.android.data.room.dao.*
import com.dzikirqu.android.model.*
import com.dzikirqu.android.model.typeconverters.AyahLineWordConverter
import com.dzikirqu.android.model.typeconverters.LanguageStringConverter
import com.dzikirqu.android.model.typeconverters.LinkConverter

@Database(entities = [PrayerBook::class, Prayer::class, PrayerData::class, Ayah::class, Surah::class, AyahLine::class, QuranLastRead::class, HadithData::class],
    version = 8)
@TypeConverters(LanguageStringConverter::class,LinkConverter::class, AyahLineWordConverter::class)
abstract class PersistenceDatabase : RoomDatabase() {

    abstract fun prayerBookDao(): PrayerBookDao
    abstract fun prayerDao(): PrayerDao
    abstract fun prayerDataDao(): PrayerDataDao
    abstract fun ayahDao(): AyahDao
    abstract fun ayahLineDao(): AyahLineDao
    abstract fun surahDao(): SurahDao
    abstract fun quranLastReadDao(): QuranLastReadDao
    abstract fun hadithDataDao(): HadithDataDao

    companion object {
        @Volatile private var instance: PersistenceDatabase? = null

        fun getDatabase(context: Context): PersistenceDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, PersistenceDatabase::class.java, "dzikirquv3")
                .fallbackToDestructiveMigration()
                .build()

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE prayerData ADD COLUMN linkr TEXT DEFAULT null ")
            }
        }
        val MIGRATION_6_7 = object : Migration(6,7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE flyersNew (id TEXT PRIMARY KEY NOT NULL DEFAULT '', title TEXT, caption TEXT, image TEXT, language TEXT)")
                database.execSQL("INSERT INTO flyersNew (id, title, caption, image, 'language') SELECT id, title, caption, image, 'language' FROM flyers")
                database.execSQL("DROP TABLE flyers")
                database.execSQL("ALTER TABLE flyersNew RENAME TO flyers")
            }
        }
    }
}