package com.wagyufari.dzikirqu.util

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wagyufari.dzikirqu.data.Prefs
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException

suspend fun <T : Any> networkCall(
    call: suspend () -> T,
    onSuccess: suspend (T) -> Unit? = {},
    onFailure: suspend (Exception)->Unit? = {},
    retries: Int? = 0
) {
    try {
        onSuccess(call())
    } catch (e: Exception) {
        if (e is HttpException && e.code() == 401 && (retries
                ?: 0) < 5 && Firebase.auth.currentUser != null
        ) {
            Prefs.accessToken = Firebase.auth.currentUser?.getIdToken(true)?.await()?.token
            networkCall(call, onSuccess, onFailure, retries?.plus(1))
        } else{
            onFailure(e)
        }
        e.printStackTrace()
    }
}