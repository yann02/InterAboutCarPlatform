package com.hnsh.dialogue.jet.common.base.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hnsh.dialogue.jet.common.base.repositorys.BaseRepository
import com.hnsh.dialogue.jet.common.state.State
import com.hnsh.dialogue.jet.common.utils.CommonUtil

/**
 * Created with Android Studio.
 * Description:
 * @author: wyy
 * @CreateDate: 2020/6/3 22:51
 */
open class BaseViewModel<T : BaseRepository> : ViewModel(){
    val loadState by lazy {
        MutableLiveData<State>()
    }

    val mRepository : T by lazy {
        (CommonUtil.getClass<T>(this))
            .getDeclaredConstructor(MutableLiveData::class.java)
            .newInstance(loadState)
    }

    override fun onCleared() {
        super.onCleared()
        mRepository.unSubscribe()
    }
}
