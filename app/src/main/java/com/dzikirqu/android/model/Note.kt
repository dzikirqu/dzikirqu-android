package com.dzikirqu.android.model

import android.content.Context
import android.os.Parcelable
import android.widget.Toast
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.annotations.SerializedName
import com.dzikirqu.android.data.ApiService
import com.dzikirqu.android.data.room.dao.getNoteDao
import com.dzikirqu.android.data.room.dao.getNotePropertyDao
import com.dzikirqu.android.model.request.toRequestModel
import com.dzikirqu.android.util.io
import com.dzikirqu.android.util.isInternetAvailable
import com.dzikirqu.android.util.networkCall
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*




@Entity(tableName = "note")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id:Int? =null,
    @ColumnInfo(name = "title")
    var title:String?="",
    @ColumnInfo(name = "subtitle")
    var subtitle:String?="",
    @SerializedName("speaker", alternate = ["presenter"])
    @ColumnInfo(name = "presenter")
    var speaker:Speaker?=null,
    @ColumnInfo(name = "location")
    var location:Location?=null,
    @ColumnInfo(name = "date")
    var date:String?="",
    @ColumnInfo(name = "tags")
    var tags:ArrayList<String>?=null,
    @ColumnInfo(name = "content")
    var content:String?=null,
    @ColumnInfo(name = "updated_date")
    var updatedDate:String?=null,
    @ColumnInfo(name = "created_date")
    var createdDate:String?=null,
    @ColumnInfo(name = "folder")
    var folder:String?=null,
    @ColumnInfo(name = "is_deleted")
    var isDeleted:Boolean?=false,
    @ColumnInfo(name = "share_id")
    var shareId:String?=null,
    @ColumnInfo(name = "is_public")
    @SerializedName("is_public")
    var isPublic:Boolean?= null,
    @Ignore
    var author:Author?= null,
) :Parcelable{
    fun getDateObject(): Date? {
        return date?.let {
            if (it.isBlank()) return null
            SimpleDateFormat("dd/MM/yyyy").parse(it)
        }
    }
    fun getUpdatedDateObject():Date?{
        return updatedDate?.let {
            if (it.isBlank()) return null
            SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(it)
        }
    }
    fun getCreatedDateObject():Date?{
        return createdDate?.let {
            if (it.isBlank()) return null
            SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(it)
        }
    }
}

@Parcelize
class Author(var uid:String?, var name:String?, var profilePicture:String?):Parcelable
@Parcelize
class Speaker(var id:Int?=null, var name:String?=null, var verified:Boolean?=null):Parcelable
@Parcelize
class Location(var id:Int?=null, var name:String?=null, var verified:Boolean?=null):Parcelable

class NoteViewMode{
    companion object{
        const val Staggered = 0
        const val Agenda = 1
        const val List = 2
    }
}
object NoteSortBy{
    const val UPDATED_DATE = 0
    const val CREATED_DATE = 1
    const val TITLE = 2
}

fun Note.delete(context:Context){
    context.io {
        context.getNoteDao().updateNote(this.apply {
            isDeleted = true
        })
    }
}

fun Note.share(context:Context, handler:(String?)->Unit){
    if (!context.isInternetAvailable()){
        Toast.makeText(
            context,
            "Please check your internet connection",
            Toast.LENGTH_SHORT
        ).show()
        handler(null)
        return
    }
    if (shareId?.isBlank() == true || shareId == null){
        context.io {
            val id = Firebase.firestore.collection("sharedNotes").add(this).await().id
            networkCall({
                ApiService.create().syncNote(this.apply {
                shareId = id
            }.toRequestModel())},{
                handler("https://dzikirqu.com/note/share/${id}")
            },{
                handler(null)
            })
        }
    } else{
        context.io{
            networkCall({
                ApiService.create().syncNote(this.toRequestModel())},{
                handler("https://dzikirqu.com/note/share/${shareId}")
            },{
                handler(null)
            })
        }
    }
}

fun Note.restore(context:Context){
    context.io {
        context.getNoteDao().updateNote(this.apply {
            isDeleted = false
        })
    }
}

fun NoteProperty.updateFolder(context:Context, new:String){
    context.io{
        val oldFolderName = this.content
        context.getNotePropertyDao()
            .updateNoteProperty(this.apply {
                content = new
            })
        context.getNoteDao().getNoteByFolderSuspend(oldFolderName).forEach {
            context.getNoteDao().updateNote(it.apply {
                it.folder = new
            })
        }
    }
}

fun NoteProperty.updateLocation(context:Context, new:String){
    context.io{
        val oldLocationName = this.content
        context.getNotePropertyDao().updateNoteProperty(this.apply {
            content = new
        })
        context.getNoteDao().getNotesSuspended().filter { it.location?.name == oldLocationName }.forEach {
            context.getNoteDao().updateNote(it.apply {
                it.location = Location(name = new)
            })
        }
    }
}
fun NoteProperty.updatePresenter(context:Context, new:String){
    context.io{
        val oldPresenterName = this.content
        context.getNotePropertyDao()
            .updateNoteProperty(this.apply {
                content = new
            })
        context.getNoteDao().getNotesSuspended().filter { it.speaker?.name == oldPresenterName }.forEach {
            context.getNoteDao().updateNote(it.apply {
                it.speaker = Speaker(name = new)
            })
        }
    }
}


@Entity(tableName = "noteFolder")
@Parcelize
data class NoteFolder(
    @PrimaryKey(autoGenerate = true)
    val id:Int? =null,
    @ColumnInfo(name = "name")
    var name:String
) :Parcelable

@Entity(tableName = "noteProperty")
@Parcelize
data class NoteProperty(
    @PrimaryKey(autoGenerate = true)
    val id:Int? =null,
    @ColumnInfo(name = "type")
    var type:NotePropertyType,
    @ColumnInfo(name = "content")
    var content:String
) :Parcelable{

    companion object{

        fun newTag(content:String?=null):NoteProperty{
            return NoteProperty(type = NotePropertyType.Tag, content = content?:"")
        }
        fun newLocation(content:String?=null):NoteProperty{
            return NoteProperty(type = NotePropertyType.Location, content = content?:"")
        }
        fun newPresenter(content:String?=null):NoteProperty{
            return NoteProperty(type = NotePropertyType.Presenter, content = content?:"")
        }
        fun newFolder(content:String?=null):NoteProperty{
            return NoteProperty(type = NotePropertyType.Folder, content = content?:"")
        }

        fun List<NoteProperty>.filterAsFolder():List<NoteProperty>{
            return filter { it.type == NotePropertyType.Folder }
        }
    }
}

enum class NotePropertyType{
    Date, Presenter, Location, Tag, Folder
}
