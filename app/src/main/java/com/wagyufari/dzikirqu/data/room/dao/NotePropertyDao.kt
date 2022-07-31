package com.wagyufari.dzikirqu.data.room.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.wagyufari.dzikirqu.data.room.NotesDatabase
import com.wagyufari.dzikirqu.model.NoteProperty

@Dao
interface NotePropertyDao {

    @Query("SELECT * FROM noteproperty")
    fun getNoteProperties(): LiveData<List<NoteProperty>>

    @Query("SELECT * FROM noteproperty")
    suspend fun getNotePropertiesSuspend(): List<NoteProperty>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putNoteProperty(noteProperty: NoteProperty)

    @Update
    suspend fun updateNoteProperty(noteProperty: NoteProperty)

    @Delete
    suspend fun deleteNoteProperty(noteProperty: NoteProperty)

    @Query("DELETE FROM noteproperty")
    suspend fun deleteAll()

    @Query("DELETE FROM noteproperty WHERE content IS \"null\"")
    suspend fun deleteNull()
}

fun Context.getNotePropertyDao():NotePropertyDao{
    return NotesDatabase.getDatabase(this).notePropertyDao()
}