package co.happy5.libraries.network.interceptor

import android.text.TextUtils
import co.happy5.libraries.local.SharedPreference
import co.happy5.libraries.local.SharedPreferenceConstant
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val accessToken =
            SharedPreference().getData<String?>(SharedPreferenceConstant.ACCESS_TOKEN, null)

        if (accessToken != null && TextUtils.isEmpty(accessToken).not()) {
            request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        }

        return chain.proceed(request)
    }
}