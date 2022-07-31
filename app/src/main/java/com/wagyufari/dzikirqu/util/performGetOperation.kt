package com.wagyufari.dzikirqu.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers

fun <T, A> performGetOperation(databaseQuery: () -> LiveData<T>,
                               networkCall: suspend () -> A,
                               saveCallResult: suspend (A) -> Unit): LiveData<T> =
    liveData(Dispatchers.IO) {
        emitSource(databaseQuery.invoke().map{ it })
        try {
            saveCallResult.invoke(networkCall.invoke())
            emitSource(databaseQuery.invoke().map { it })
        } catch (e:Exception){
            e.printStackTrace()
        }
    }



