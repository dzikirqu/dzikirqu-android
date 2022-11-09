package com.dzikirqu.android.data.room.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzikirqu.android.data.room.BookmarkDatabase
import com.dzikirqu.android.model.BookmarkGroup

@Dao
interface BookmarkGroupDao {

    @Query("SELECT * FROM bookmarkGroup WHERE type IS :type")
    fun getGroupByType(type:String): LiveData<List<BookmarkGroup>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putGroup(bookmarkGroup: BookmarkGroup)
}

fun Context.getBookmarkGroupDao():BookmarkGroupDao{
    return BookmarkDatabase.getDatabase(this).bookmarkGroupDao()
}