package com.dzikirqu.android.data.room.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.dzikirqu.android.data.room.NotesDatabase
import com.dzikirqu.android.model.Note

@Dao
interface NoteDao {

//    @Query("SELECT * FROM note WHERE is_deleted == :isDeleted  ORDER BY updated_date DESC ")
//     fun getNoteByLastUpdatedDesc(isDeleted:Boolean? = false): LiveData<List<Note>>
//    @Query("SELECT * FROM note WHERE is_deleted == :isDeleted  ORDER BY created_date DESC ")
//     fun getNoteByCreatedDateDesc(isDeleted:Boolean? = false): LiveData<List<Note>>
//    @Query("SELECT * FROM note WHERE is_deleted == :isDeleted  ORDER BY title COLLATE NOCASE ASC")
//     fun getNoteByTitleDesc(isDeleted:Boolean? = false): LiveData<List<Note>>
//    @Query("SELECT * FROM note WHERE is_deleted == :isDeleted  ORDER BY updated_date Asc")
//     fun getNoteByLastUpdatedAsc(isDeleted:Boolean? = false): LiveData<List<Note>>
//    @Query("SELECT * FROM note WHERE is_deleted == :isDeleted  ORDER BY created_date Asc")
//     fun getNoteByCreatedDateAsc(isDeleted:Boolean? = false): LiveData<List<Note>>
//    @Query("SELECT * FROM note WHERE is_deleted == :isDeleted  ORDER BY title COLLATE NOCASE desc")
//     fun getNoteByTitleAsc(isDeleted:Boolean? = false): LiveData<List<Note>>

     @Query("SELECT * FROM note WHERE is_deleted IS :isDeleted")
     fun getNotes(isDeleted:Boolean? = false):LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE folder IS :folder AND is_deleted IS :isDeleted")
    suspend fun getNoteByFolderSuspend(folder:String,isDeleted:Boolean? = false): List<Note>

    @Query("SELECT * FROM note WHERE folder IS :folder AND is_deleted IS :isDeleted")
    fun getNoteByFolder(folder:String?, isDeleted:Boolean? = false): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE id IS :id")
    suspend fun getNoteByIdSuspend(id:Int): Note?

    @Query("SELECT * FROM note WHERE id IS :id")
    fun getNoteById(id:Int): LiveData<Note?>
    @Query("SELECT * FROM note WHERE id IS :id")
    fun getNoteByIds(id:Int): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE is_deleted IS :isDeleted")
     fun getDeletedNote(isDeleted:Boolean? = true): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE is_deleted == :isDeleted")
    suspend fun getNotesSuspended(isDeleted:Boolean? = false): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putNote(note: Note):Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putNotes(notes:List<Note>)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note WHERE title LIKE :query AND is_deleted IS :isDeleted")
    suspend fun searchNoteByTitle(query:String,isDeleted:Boolean? = false): List<Note>
    @Query("SELECT * FROM note WHERE subtitle LIKE :query AND is_deleted IS :isDeleted")
    suspend fun searchNoteBySubtitle(query:String,isDeleted:Boolean? = false): List<Note>
    @Query("SELECT * FROM note WHERE content LIKE :query AND is_deleted IS :isDeleted")
    suspend fun searchNoteByContent(query:String,isDeleted:Boolean? = false): List<Note>
    @Query("SELECT * FROM note WHERE presenter LIKE :query AND is_deleted IS :isDeleted")
    suspend fun searchNoteBySpeaker(query:String,isDeleted:Boolean? = false): List<Note>
    @Query("SELECT * FROM note WHERE location LIKE :query AND is_deleted IS :isDeleted")
    suspend fun searchNoteByLocation(query:String,isDeleted:Boolean? = false): List<Note>
    @Query("SELECT * FROM note WHERE tags LIKE :query AND is_deleted IS :isDeleted")
    suspend fun searchNoteByTags(query:String,isDeleted:Boolean? = false): List<Note>

    @Query("DELETE FROM note")
    suspend fun deleteAll()
}

fun Context.getNoteDao(): NoteDao {
    return NotesDatabase.getDatabase(this).noteDao()
}