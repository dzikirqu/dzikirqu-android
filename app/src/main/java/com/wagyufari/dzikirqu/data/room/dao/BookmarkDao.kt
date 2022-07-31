package com.wagyufari.dzikirqu.data.room.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.wagyufari.dzikirqu.data.room.BookmarkDatabase
import com.wagyufari.dzikirqu.model.Bookmark
import com.wagyufari.dzikirqu.model.BookmarkHighlightUpdate

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks")
    fun getBookmarks(): LiveData<List<Bookmark>>

    @Query("SELECT * FROM bookmarks WHERE highlighted IS :highlighted")
    fun getHighlights(highlighted:Boolean?=true): LiveData<List<Bookmark>>
    @Query("SELECT * FROM bookmarks WHERE highlighted IS :highlighted")
    suspend fun getHighlightsSuspend(highlighted:Boolean?=true): List<Bookmark>

    @Query("SELECT * FROM bookmarks WHERE type IS :type")
    fun getBookmarksByType(type:String): LiveData<List<Bookmark>>

    @Query("SELECT * FROM bookmarks WHERE type IS :type")
    suspend fun getBookmarksByTypeSuspend (type:String): List<Bookmark>

    @Query("SELECT * FROM bookmarks WHERE idString IS :ids")
    suspend fun getBookmarkByIdSuspend(ids:String): Bookmark?

    @Query("SELECT * FROM bookmarks WHERE idString IS :ids")
    fun getBookmarkById(ids:String): LiveData<Bookmark?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putBookmark(bookmark:Bookmark)

    @Query("DELETE FROM bookmarks WHERE :ids IS idString AND type IS :type")
    suspend fun deleteBookmark(ids:String, type:String)

    @Update(entity = Bookmark::class)
    suspend fun updateHighlight(update: BookmarkHighlightUpdate)
}

fun Context.getBookmarkDao():BookmarkDao{
    return BookmarkDatabase.getDatabase(this).bookmarkDao()
}