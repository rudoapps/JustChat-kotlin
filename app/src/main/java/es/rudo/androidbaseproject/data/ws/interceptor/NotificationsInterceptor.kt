package es.rudo.androidbaseproject.data.ws.interceptor

import es.rudo.androidbaseproject.data.ws.api.Config
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NotificationsInterceptor : Interceptor {

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = getRequestWithHeaders(chain)
        return chain.proceed(request)
    }

    private fun getRequestWithHeaders(chain: Interceptor.Chain): Request {
        val request = chain.request()
        val newRequest = request.newBuilder().apply {
            addHeader(Config.TYPE_ITEM_AUTHORIZATION, "key=${Config.FCM_TOKEN}")
            addHeader(Config.CONTENT_TYPE, Config.CONTENT_VALUE)
        }

        return newRequest.build()
    }
}
