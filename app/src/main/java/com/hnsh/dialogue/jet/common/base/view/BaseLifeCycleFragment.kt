package com.hnsh.dialogue.jet.common.base.view

import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.kingja.loadsir.callback.SuccessCallback
import com.hnsh.dialogue.jet.common.base.viewmodels.BaseViewModel
import com.hnsh.dialogue.jet.common.callback.EmptyCallBack
import com.hnsh.dialogue.jet.common.callback.ErrorCallBack
import com.hnsh.dialogue.jet.common.callback.LoadingCallBack
import com.hnsh.dialogue.jet.common.getAppViewModel
import com.hnsh.dialogue.jet.common.state.State
import com.hnsh.dialogue.jet.common.state.StateType
import com.hnsh.dialogue.jet.modules.app.AppViewModel

/**
 * Created with Android Studio.
 * Description:
 * @author: wyy
 * @CreateDate: 2020/6/3 23:19
 */
abstract class BaseLifeCycleFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> :
    BaseFragment<VM, DB>() {
    val appViewModel: AppViewModel by lazy { getAppViewModel() }

    override fun initView() {
        showLoading()
        mViewModel.loadState.observe(this, observer)
        initDataObserver()
    }

    open fun initDataObserver() {}

    open fun showLoading() {
        loadService.showCallback(LoadingCallBack::class.java)
    }

    open fun showSuccess() {
        loadService.showCallback(SuccessCallback::class.java)
    }

    open fun showError(msg: String) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
        loadService.showCallback(ErrorCallBack::class.java)
    }


    open fun showEmpty() {
        loadService.showCallback(EmptyCallBack::class.java)
    }

    private val observer by lazy {
        Observer<State> {
            it?.let {
                when (it.code) {
                    StateType.SUCCESS -> showSuccess()
                    StateType.LOADING -> showLoading()
                    StateType.ERROR -> showError("网络异常")
                    StateType.NETWORK_ERROR -> showError("网络异常")
                    StateType.EMPTY -> showEmpty()
                }
            }
        }
    }


    override fun reLoad() {
        showLoading()
        initData()
        initDataObserver()
        super.reLoad()
    }
}