package com.dzikirqu.android.data.room.dao

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dzikirqu.android.data.room.AppDatabase
import com.dzikirqu.android.model.Khatam

@Dao
interface KhatamDao {

    @Query("SELECT * FROM khatam")
    fun getKhatam(): LiveData<List<Khatam>>

    @Query("SELECT * FROM khatam")
    suspend fun getKhatamSuspend(): List<Khatam>

    @Query("SELECT * FROM khatam WHERE state IS :state")
    suspend fun getKhatamByState(state:String):Khatam?

    @Insert
    fun putKhatam(khatam: Khatam)

    @Update
    fun updateKhatam(khatam: Khatam)
}

fun Context.getKhatamDao():KhatamDao{
    return AppDatabase.getDatabase(this).khatamDao()
}
fun Fragment.getKhatamDao():KhatamDao{
    return AppDatabase.getDatabase(requireActivity()).khatamDao()
}

