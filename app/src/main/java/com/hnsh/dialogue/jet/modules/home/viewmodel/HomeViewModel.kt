package com.hnsh.dialogue.jet.modules.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hnsh.dialogue.jet.common.base.viewmodels.BaseViewModel
import com.hnsh.dialogue.jet.common.base.models.HttpBaseBean
import com.hnsh.dialogue.jet.common.initiateRequest
import com.hnsh.dialogue.jet.modules.home.repository.HomeRepository

class HomeViewModel : BaseViewModel<HomeRepository>() {
    val mSearchPlacesData: MutableLiveData<HttpBaseBean> = MutableLiveData()
    val mHttpBaseBean: MutableLiveData<HttpBaseBean> = MutableLiveData()

    fun searchPlaces(query: String) {
        initiateRequest({
            mSearchPlacesData.value = mRepository.searchPlaces()
        }, loadState)
    }

    /**
     * 获取apk信息
     */
    fun getApkInfo() {
        initiateRequest({
            mHttpBaseBean.value = mRepository.getApkInfo()
            val a:HttpBaseBean= mHttpBaseBean.value!!
            Log.d("wyy","getApkInfo mHttpBaseBean.value:${a.bodyContent}")
        }, loadState)
    }
}