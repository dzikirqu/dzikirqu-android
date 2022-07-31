package com.wagyufari.dzikirqu.model.request

import android.os.Parcelable
import com.wagyufari.dzikirqu.model.Location
import com.wagyufari.dzikirqu.model.Note
import com.wagyufari.dzikirqu.model.Speaker
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteRequestModel(
    var local_id:Int? =null,
    var title:String?="",
    var subtitle:String?="",
    var presenter: Speaker?=null,
    var location: Location?=null,
    var date:String?="",
    var tags: ArrayList<String>?=null,
    var content:String?=null,
    var updated_date:String?=null,
    var created_date:String?=null,
    var folder:String?=null,
    var is_deleted:Boolean?=false,
    var share_id:String?=null,
    var is_public:Boolean?=false
) : Parcelable{


}

fun Note.toRequestModel():NoteRequestModel{
    return NoteRequestModel(
        local_id = id,
        title = title,
        subtitle = subtitle,
        presenter = speaker,
        location = location,
        date = date,
        tags = tags,
        content = content,
        updated_date = updatedDate,
        created_date = createdDate,
        folder = folder,
        is_deleted = isDeleted,
        share_id = shareId,
        is_public = isPublic
    )
}