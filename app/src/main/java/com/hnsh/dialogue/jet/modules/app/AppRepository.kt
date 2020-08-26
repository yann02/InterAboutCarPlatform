package com.hnsh.dialogue.jet.modules.app

import androidx.lifecycle.MutableLiveData
import com.hnsh.dialogue.jet.common.base.repositorys.ApiRepository
import com.hnsh.dialogue.jet.common.state.State

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @CreateDate: 2020/6/4 12:25
 */

class AppRepository(var loadState: MutableLiveData<State>) : ApiRepository() {
//    suspend fun queryAllChoosePlace() = RoomHelper.queryAllChoosePlace(loadState)
}