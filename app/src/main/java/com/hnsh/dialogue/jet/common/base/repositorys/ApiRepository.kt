package com.hnsh.dialogue.jet.common.base.repositorys

import com.hnsh.dialogue.jet.network.ApiService
import com.hnsh.dialogue.jet.network.RetrofitFactory


/**
 * Created with Android Studio.
 * Description:
 * @author: wyy
 * @CreateDate: 2020/6/3 22:54
 */
abstract class ApiRepository : BaseRepository() {
    protected val apiService: ApiService by lazy {
        RetrofitFactory.instance.createRetrofit(ApiService::class.java)
    }
}