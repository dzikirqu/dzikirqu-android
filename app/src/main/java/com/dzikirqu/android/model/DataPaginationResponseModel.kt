package com.dzikirqu.android.model

import com.google.gson.annotations.SerializedName

class DataPaginationResponseModel<T>(data: List<T>? = null, pagination: PaginationResponseModel? = null) {

    @SerializedName("data")
    var data: List<T>? = data
        private set
    @SerializedName("pagination")
    var pagination: PaginationResponseModel? = pagination
        private set
        get() { return if (field == null) PaginationResponseModel() else field}


    fun setData(data: ArrayList<T>): DataPaginationResponseModel<T> {
        this.data = data
        return this
    }

    fun setPagination(pagination: PaginationResponseModel): DataPaginationResponseModel<T> {
        this.pagination = pagination
        return this
    }

    data class PaginationResponseModel(@SerializedName("last_skip")
                                       val last_skip: Int? = null)
}