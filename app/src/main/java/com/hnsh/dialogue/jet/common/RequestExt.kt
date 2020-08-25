package com.hnsh.dialogue.jet.common

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hnsh.dialogue.jet.common.base.repositorys.BaseRepository
import com.hnsh.dialogue.jet.common.base.viewmodels.BaseViewModel
import com.hnsh.dialogue.jet.common.state.State
import com.hnsh.dialogue.jet.network.NetExceptionHandle
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.launch

/**
 * Created with Android Studio.
 * Description:
 * @author: wyy
 * @CreateDate: 2020/6/8 21:30
 */
fun <T : BaseRepository> BaseViewModel<T>.initiateRequest(
    block: suspend () -> Unit,
    loadState: MutableLiveData<State>
) {
    viewModelScope.launch {
        runCatching {
            block()
            Log.d("wyy", "success")
        }.onSuccess {
            Log.d("wyy", "success1")
        }.onFailure {
            it.printStackTrace()
            Log.d("wyy", "fail:${it.message}")
            NetExceptionHandle.handleException(it, loadState)
        }
    }
}

fun requestByPermissionOnFragment(block: () -> Unit, context: Fragment, arr: List<String>) {
    PermissionX.init(context)
        .permissions(arr)
        .onExplainRequestReason { scope, deniedList ->
            val message = "应用权限申请"
            val ok = "确定"
            scope.showRequestReasonDialog(deniedList, message, ok)
        }
        .onForwardToSettings { scope, deniedList ->
            val message = "您需要去设置当中同意请求权限"
            val ok = "确定"
            scope.showForwardToSettingsDialog(deniedList, message, ok)
        }
        .request { _, _, _ ->
            Log.d("wyy", "走了网络请求的方法")
            block()
        }
}

fun requestByPermissionOnActivity(block: () -> Unit, context: FragmentActivity, arr: List<String>) {
    PermissionX.init(context)
        .permissions(arr)
        .onExplainRequestReason { scope, deniedList ->
            val message = "加载网络数据需要您同意网络请求权限"
            val ok = "确定"
            scope.showRequestReasonDialog(deniedList, message, ok)
        }
        .onForwardToSettings { scope, deniedList ->
            val message = "您需要去设置当中同意网络请求权限"
            val ok = "确定"
            scope.showForwardToSettingsDialog(deniedList, message, ok)
        }
        .request { _, _, _ ->
            Log.d("wyy", "走了网络请求的方法")
            block()
        }
}