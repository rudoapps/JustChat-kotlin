package es.rudo.firebasechat.data.ws.interceptor

import es.rudo.firebasechat.data.ws.api.Config
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
            addHeader(
                Config.TYPE_ITEM_AUTHORIZATION,
                "${Config.HTTP_CLIENT_AUTHORIZATION}${Config.SERVER_KEY}"
            )
            addHeader(Config.TYPE_ITEM_CONTENT_TYPE, Config.CONTENT_TYPE)
        }

        return newRequest.build()
    }
}
