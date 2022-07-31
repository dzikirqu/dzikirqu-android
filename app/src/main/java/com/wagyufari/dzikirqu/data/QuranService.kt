package com.wagyufari.dzikirqu.data

import com.wagyufari.dzikirqu.QuranApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface QuranService {
    companion object Factory {
        fun create(): QuranService {

            val httpClient = OkHttpClient.Builder().apply {
//                addInterceptor(
//                    ChuckInterceptor(BaseApplication.instance)
//                        .showNotification(true)
//                )
                connectTimeout(60, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
            }

            return Retrofit.Builder()
                .baseUrl("https://api.quran.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(QuranService::class.java)
        }
    }

    @GET("/api/v4/verses/by_page/{page}")
    suspend fun getVersesByPage(
        @Path("page") juz:Int,
        @Query("language") language:String,
        @Query("page") page:Int,
        @Query("per_page") perPage:Int,
        @Query("words") words:Boolean,
    ): QuranApi

}