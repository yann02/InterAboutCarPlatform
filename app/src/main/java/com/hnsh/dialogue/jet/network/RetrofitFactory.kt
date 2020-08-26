package com.hnsh.dialogue.jet.network

import android.util.Log
import com.hnsh.dialogue.BuildConfig
import okhttp3.*
import okio.Buffer
import okio.BufferedSource
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


/**
 * Created with Android Studio.
 * Description:
 * @author: wyy
 * @CreateDate: 2020/6/3 22:10
 */
private val DEFAULT_TIMEOUT: Long = 30
private val TAG = "RetrofitFactory"

class RetrofitFactory private constructor() {
    private val retrofit: Retrofit
//    private val networkMonitor: LiveNetworkMonitor? = null

    fun <T> createRetrofit(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(configClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    companion object {
        val instance by lazy {
            RetrofitFactory()
        }
    }

    private fun configClient(): OkHttpClient? {
        val okHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        if (okHttpClient == null) {
            Log.e(TAG, "okHttpClient is null")
        }
//        okHttpClient.sslSocketFactory(SSLConection.createSSLSocketFactory())
//        okHttpClient.hostnameVerifier(TrustAllHostnameVerifier())
//        //为所有请求添加网络状态配置的拦截器
//        val networkIntercept: Interceptor = object : Interceptor {
//            @Throws(IOException::class)
//            fun intercept(chain: Interceptor.Chain): Response? {
//                val connected: Boolean = networkMonitor.isConnected()
//                LogUtil.d(TAG, "one Is connetcted !$connected")
//                return if (connected) {
//                    //sfq 2017-0327 统一添加请求的请求头消息，如果有必要的话
//                    var language = "zh"
//                    if (null != context && "en" == context.getResources().getConfiguration().locale.getLanguage()) {
//                        language = "en" //防止切换中英文之外的语言
//                    }
//                    val request: Request = chain.request()
//                            .newBuilder()
//                            .addHeader("language", language)
//                            .build()
//                    LogUtil.d(TAG, "request:" + chain.request().toString() + " language:" + language)
//                    chain.proceed(request)
//                } else {
//                    throw NoNetworkException()
//                }
//            }
//        }
//
//        // Log信息拦截器
//        if (true) {
//            val loggingIntercept: Interceptor = object : Interceptor() {
//                @Throws(IOException::class)
//                fun intercept(chain: Interceptor.Chain): Response? {
//                    val connected: Boolean = networkMonitor.isConnected()
//                    Log.d("NetWotk State:", "two Is connetcted !$connected")
//                    return if (connected) {
//                        val request: Request = chain.request()
//                        val response: Response = chain.proceed(request)
//                        val responseBody: ResponseBody = response.body()
//                        val source: BufferedSource = responseBody.source()
//                        source.request(Long.MAX_VALUE) // Buffer the entire body.
//                        val buffer: Buffer = source.buffer()
//                        val UTF8: Charset = Charset.forName("UTF-8")
//                        Log.v("RESPONE_JSON", buffer.clone().readString(UTF8))
//                        Log.v("REQUEST_URL", request.toString())
//                        response
//                    } else {
//                        throw NoNetworkException()
//                    }
//                }
//            }
//            okHttpClient.addInterceptor(loggingIntercept)
//        }
        okHttpClient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        okHttpClient.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS) // socket timeout
//        okHttpClient.addNetworkInterceptor(networkIntercept)
        return okHttpClient.build()
    }
}