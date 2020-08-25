package com.hnsh.dialogue.jet.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hnsh.dialogue.jet.common.base.viewmodels.BaseViewModel
import com.hnsh.dialogue.jet.common.base.models.HttpBaseBean
import com.hnsh.dialogue.jet.common.initiateRequest
import com.hnsh.dialogue.jet.main.repository.SearchPlaceRepository

class SearchPlaceViewModel : BaseViewModel<SearchPlaceRepository>() {
    val mSearchPlacesData: MutableLiveData<HttpBaseBean> = MutableLiveData()

    fun searchPlaces(query: String) {
        initiateRequest({
            mSearchPlacesData.value = mRepository.searchPlaces()
        }, loadState)
    }
}