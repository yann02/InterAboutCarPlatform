package com.hnsh.dialogue.jet.modules.main.repository

import androidx.lifecycle.MutableLiveData
import com.hnsh.dialogue.jet.common.base.repositorys.ApiRepository
import com.hnsh.dialogue.jet.common.state.State

class MainRepository(var loadState: MutableLiveData<State>): ApiRepository() {
}