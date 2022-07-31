package com.wagyufari.dzikirqu.model.response

import com.wagyufari.dzikirqu.model.Location
import com.wagyufari.dzikirqu.model.Note
import com.wagyufari.dzikirqu.model.Speaker

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