package com.hnsh.dialogue.jet.network

import com.hnsh.dialogue.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created with Android Studio.
 * Description:
 * @author: wyy
 * @CreateDate: 2020/6/3 22:10
 */
class RetrofitFactory private constructor() {
    private val retrofit : Retrofit

    fun <T> createRetrofit(clazz: Class<T>) : T {
        return retrofit.create(clazz)
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    companion object {
        val instance by lazy {
            RetrofitFactory()
        }
    }
}