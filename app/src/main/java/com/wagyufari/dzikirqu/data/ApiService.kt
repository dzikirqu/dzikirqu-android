package com.wagyufari.dzikirqu.data

import android.content.Context
import android.text.TextUtils
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.wagyufari.dzikirqu.BuildConfig
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseApplication
import com.wagyufari.dzikirqu.data.ChuckerUtil.createChuckInterceptor
import com.wagyufari.dzikirqu.model.request.NoteRequestModel
import com.wagyufari.dzikirqu.model.response.AuthResponseModel
import com.wagyufari.dzikirqu.model.response.DataModel
import com.wagyufari.dzikirqu.model.response.NoteResponseModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object ChuckerUtil {

    fun Context.createChuckInterceptor(): Interceptor {
        val chuckerCollector = ChuckerCollector(
            context = this,
            showNotification = true
        )

        return ChuckerInterceptor.Builder(context = this)
            .collector(collector = chuckerCollector)
            .maxContentLength(250000L)
            .build()
    }
}

interface ApiService {
    companion object Factory {
        fun create(): ApiService {

            val authInterceptor = Interceptor { chain ->
                var request = chain.request()
                var response: Response? = null
                val accessToken = Prefs.accessToken

                if (!TextUtils.isEmpty(accessToken)) {
                    request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $accessToken")
                        .build()
                    response = chain.proceed(request)
                }
                response ?: chain.proceed(request)
            }

            // SSL
            var sslContext: SSLContext? = null
            var trustManager: X509TrustManager? = null

            try {
                val cf: CertificateFactory? = CertificateFactory.getInstance("X.509")
                BaseApplication.instance?.resources?.openRawResource(R.raw.certificate)?.let { cert ->
                    val ca = cf?.generateCertificate(cert)
                    cert.close()

                    val keyStoreType = KeyStore.getDefaultType()
                    val keyStore = KeyStore.getInstance(keyStoreType)

                    keyStore.load(null, null)
                    keyStore.setCertificateEntry("ca", ca)
                    trustManager = getTrustManager(keyStore)

                    trustManager?.let { trustManager->
                        sslContext = SSLContext.getInstance("TLS")
                        sslContext?.init(null, arrayOf<TrustManager>(trustManager), null)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


            val httpClient = OkHttpClient.Builder().apply {
                connectTimeout(60, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
            }

            sslContext?.let { ssl->
                trustManager?.let { trust->
                    httpClient.sslSocketFactory(ssl.socketFactory, trust)
                }
            }

            httpClient.addInterceptor(authInterceptor)
            if (BuildConfig.DEBUG) {
                BaseApplication.instance?.createChuckInterceptor()?.let {
                    httpClient
                        .addInterceptor(it)
                }
            }


            return Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(ApiService::class.java)
        }
        fun getTrustManager(keyStore: KeyStore): X509TrustManager {
            val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)

            val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                ("Unexpected default trust managers:"
                        + trustManagers.contentToString())
            }

            return trustManagers[0] as X509TrustManager
        }
    }


//    @GET("v1/auth")
//    suspend fun getAuth():AuthResponseModel

    @POST("v1/notes/sync-bulk")
    suspend fun syncNotes(@Body notes:List<NoteRequestModel>):Any
    @POST("v1/notes/sync")
    suspend fun syncNote(@Body notes:NoteRequestModel):AuthResponseModel
    @GET("v1/notes")
    suspend fun getNotes(): DataModel<List<NoteResponseModel>>
    @GET("v1/notes/public")
    suspend fun getPublicNotes(): DataModel<List<NoteResponseModel>>
    @GET("v1/note")
    suspend fun getNoteByShareId(@Query("share_id") shareId:String?=null): DataModel<NoteResponseModel>
    @GET("v1/note")
    suspend fun getNoteById(@Query("id") id:Int?=null): DataModel<NoteResponseModel>

}

data class FlyerRequestModel(var title: String? = null,
                             var caption: String? = null,
                             var image: String? = "",
                             var language: String? = "bahasa")