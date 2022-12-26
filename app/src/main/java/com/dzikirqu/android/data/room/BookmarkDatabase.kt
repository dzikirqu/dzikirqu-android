package com.dzikirqu.android.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dzikirqu.android.data.room.dao.BookmarkDao
import com.dzikirqu.android.data.room.dao.BookmarkGroupDao
import com.dzikirqu.android.model.Bookmark
import com.dzikirqu.android.model.BookmarkGroup
import com.dzikirqu.android.model.typeconverters.StringArrayConverter

@Database(
    entities = [Bookmark::class, BookmarkGroup::class],
    version = 6
)
@TypeConverters(StringArrayConverter::class)
abstract class BookmarkDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao
    abstract fun bookmarkGroupDao(): BookmarkGroupDao

    companion object {
        @Volatile
        private var instance: BookmarkDatabase? = null

        fun getDatabase(context: Context): BookmarkDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, BookmarkDatabase::class.java, "bookmarks")
                .fallbackToDestructiveMigration()
                .build()
    }
}