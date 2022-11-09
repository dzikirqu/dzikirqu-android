package com.dzikirqu.android.model.response

import com.dzikirqu.android.model.Location
import com.dzikirqu.android.model.Note
import com.dzikirqu.android.model.Speaker

class NoteResponseModel(var local_id:Int? =null,
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
                        var is_deleted:Boolean?=false,)

fun NoteResponseModel.toNote(): Note {
    return Note(
        id = local_id,
        title = title,
        subtitle = subtitle,
        speaker = presenter,
        location = location,
        date = date,
        tags = tags,
        content = content,
        updatedDate = updated_date,
        createdDate = created_date,
        folder = folder,
        isDeleted = is_deleted,
    )
}