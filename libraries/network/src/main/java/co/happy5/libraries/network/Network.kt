package co.happy5.libraries.network

import android.content.Context
import co.happy5.libraries.network.interceptor.AuthInterceptor
import co.happy5.libraries.network.interceptor.CommonInterceptor
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object Network {

    fun retrofitClient(url: String = BuildConfig.SERVER_URL, context: Context): Retrofit {
        return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient(context))
                .build()
    }

    private fun okHttpClient(context: Context): OkHttpClient {
        val httpOkClient = OkHttpClient.Builder()

        // SSL
        var sslContext: SSLContext? = null
        var trustManager: X509TrustManager? = null

        try {
            val cf: CertificateFactory? = CertificateFactory.getInstance("X.509")
            val cert: InputStream = context.resources.openRawResource(R.raw.happy5certificate)

            val ca = cf?.generateCertificate(cert)
            cert.close()

            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType)

            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)
            trustManager = getTrustManager(keyStore)

            sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (sslContext != null && trustManager != null) {
            httpOkClient.sslSocketFactory(sslContext.socketFactory, trustManager)
        }

        if (BuildConfig.DEBUG) {
            httpOkClient
                    .addInterceptor(createLoggingInterceptor())
                    .addInterceptor(ChuckInterceptor(context).showNotification(true))
        }

        return httpOkClient
                .addInterceptor(AuthInterceptor())
                .addInterceptor(CommonInterceptor())
                .pingInterval(30, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .build()
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    private fun getTrustManager(keyStore: KeyStore): X509TrustManager {
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