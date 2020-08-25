package com.hnsh.dialogue.jet.network

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonParseException
import com.hnsh.dialogue.jet.common.state.State
import com.hnsh.dialogue.jet.common.state.StateType
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Created with Android Studio.
 * Description:
 * @author: wyy
 * @CreateDate: 2020/6/8 21:31
 */
object NetExceptionHandle {
    fun handleException(e: Throwable?, loadState: MutableLiveData<State>){
        e?.let {
            when (it) {
                is HttpException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR))
                }
                is ConnectException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR))
                }
                is ConnectTimeoutException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR))
                }
                is UnknownHostException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR))
                }
                is JsonParseException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR))
                }
                is NoClassDefFoundError -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR))
                }
            }
        }
    }
}