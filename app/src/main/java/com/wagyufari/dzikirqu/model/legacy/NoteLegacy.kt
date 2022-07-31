package com.wagyufari.dzikirqu.model.legacy

import com.google.gson.annotations.SerializedName
import com.wagyufari.dzikirqu.model.Author

data class NoteLegacy(
    var id:Int? =null,
    var title:String?="",
    var subtitle:String?="",
    @SerializedName("speaker", alternate = ["presenter"])
    var speaker: String?=null,
    var location: String?=null,
    var date:String?="",
    var tags: ArrayList<String>?=null,
    var content:String?=null,
    var updatedDate:String?=null,
    var createdDate:String?=null,
    var folder:String?=null,
    var isDeleted:Boolean?=false,
    var sharedReference:String?=null,
    var author: Author?= null,
)