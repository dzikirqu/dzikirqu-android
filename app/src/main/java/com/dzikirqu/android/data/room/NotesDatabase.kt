package com.dzikirqu.android.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dzikirqu.android.data.room.dao.NoteDao
import com.dzikirqu.android.data.room.dao.NotePropertyDao
import com.dzikirqu.android.model.Note
import com.dzikirqu.android.model.NoteProperty
import com.dzikirqu.android.model.typeconverters.LocationConverter
import com.dzikirqu.android.model.typeconverters.SpeakerConverter
import com.dzikirqu.android.model.typeconverters.StringArrayConverter

@Database(
    entities = [NoteProperty::class, Note::class],
    version = 20
)
@TypeConverters(StringArrayConverter::class, LocationConverter::class, SpeakerConverter::class)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notePropertyDao(): NotePropertyDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: NotesDatabase? = null

        fun getDatabase(context: Context): NotesDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE note ADD COLUMN newVariable TEXT")
            }
        }
        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE note_backup(id Integer, title TEXT,subtitle TEXT,presenter TEXT,location TEXT,date TEXT,tags TEXT,content TEXT,updated_date TEXT,created_date TEXT)")
                database.execSQL("INSERT INTO note_backup SELECT id,title,subtitle,presenter,location,date,tag,content,updated_date,created_date FROM note")
                database.execSQL("DROP TABLE note")
                database.execSQL("CREATE TABLE note(id INTEGER PRIMARY KEY, title TEXT,subtitle TEXT,presenter TEXT,location TEXT,date TEXT,tags TEXT,content TEXT,updated_date TEXT,created_date TEXT)")
                database.execSQL("INSERT INTO note SELECT id,title,subtitle,presenter,location,date,tags,content,updated_date,created_date FROM note_backup")
                database.execSQL("DROP TABLE note_backup")
            }
        }

        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE note ADD COLUMN folder TEXT")
            }
        }
        val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE note ADD COLUMN is_deleted INTEGER DEFAULT 0")
            }
        }
        val MIGRATION_13_14 = object : Migration(13, 14) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE notefolder")
            }
        }
        val MIGRATION_14_15 = object : Migration(14, 15) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE note ADD COLUMN shared_reference TEXT")
            }
        }
        val MIGRATION_15_16 = object : Migration(15, 16) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE note ADD COLUMN author TEXT")
            }
        }
        val MIGRATION_16_17 = object : Migration(16, 17) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE note_backup(id Integer, title TEXT,subtitle TEXT,presenter TEXT,location TEXT,date TEXT,tags TEXT,content TEXT,updated_date TEXT,created_date TEXT, folder TEXT, is_deleted INTEGER DEFAULT 0, shared_reference TEXT)")
                database.execSQL("INSERT INTO note_backup SELECT id,title,subtitle,presenter,location,date,tags,content,updated_date,created_date, folder, is_deleted, shared_reference FROM note")
                database.execSQL("DROP TABLE note")
                database.execSQL("CREATE TABLE note(id INTEGER PRIMARY KEY, title TEXT,subtitle TEXT,presenter TEXT,location TEXT,date TEXT,tags TEXT,content TEXT,updated_date TEXT,created_date TEXT, folder TEXT, is_deleted INTEGER DEFAULT 0, shared_reference TEXT)")
                database.execSQL("INSERT INTO note SELECT id,title,subtitle,presenter,location,date,tags,content,updated_date,created_date, folder, is_deleted, shared_reference FROM note_backup")
                database.execSQL("DROP TABLE note_backup")
            }
        }
        val MIGRATION_17_18 = object : Migration(17, 18) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
        val MIGRATION_18_19 = object : Migration(18, 19) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE note_backup(id Integer, title TEXT,subtitle TEXT,presenter TEXT,location TEXT,date TEXT,tags TEXT,content TEXT,updated_date TEXT,created_date TEXT, folder TEXT, is_deleted INTEGER DEFAULT 0, shared_reference TEXT)")
                database.execSQL("INSERT INTO note_backup SELECT id,title,subtitle,presenter,location,date,tags,content,updated_date,created_date, folder, is_deleted, shared_reference FROM note")
                database.execSQL("DROP TABLE note")
                database.execSQL("CREATE TABLE note(id INTEGER PRIMARY KEY, title TEXT,subtitle TEXT,presenter TEXT,location TEXT,date TEXT,tags TEXT,content TEXT,updated_date TEXT,created_date TEXT, folder TEXT, is_deleted INTEGER DEFAULT 0, share_id TEXT)")
                database.execSQL("INSERT INTO note SELECT id,title,subtitle,presenter,location,date,tags,content,updated_date,created_date, folder, is_deleted, shared_reference FROM note_backup")
                database.execSQL("DROP TABLE note_backup")
            }
        }
        val MIGRATION_19_20 = object : Migration(19, 20) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE note ADD COLUMN is_public INTEGER DEFAULT 0")
            }
        }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, NotesDatabase::class.java, "notes")
                .addMigrations(MIGRATION_9_10,
                    MIGRATION_10_11,
                    MIGRATION_11_12,
                    MIGRATION_12_13,
                    MIGRATION_13_14,
                    MIGRATION_14_15,
                    MIGRATION_15_16,
                    MIGRATION_16_17,
                    MIGRATION_17_18,
                    MIGRATION_18_19,
                    MIGRATION_19_20)
                .build()
    }
}