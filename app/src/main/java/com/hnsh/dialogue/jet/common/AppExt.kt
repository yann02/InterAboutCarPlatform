package com.hnsh.dialogue.jet.common

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hnsh.dialogue.jet.modules.app.AppEventViewModel
import com.hnsh.dialogue.jet.modules.app.AppViewModel
import com.hnsh.dialogue.jet.common.base.BaseApplication

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @CreateDate: 2020/6/9 23:29
 */
fun AppCompatActivity.getAppViewModel(): AppViewModel {
    BaseApplication.instance.let {
        return it.getAppViewModelProvider().get(AppViewModel::class.java)
    }
}

fun Fragment.getAppViewModel(): AppViewModel {
    BaseApplication.instance.let {
        return it.getAppViewModelProvider().get(AppViewModel::class.java)
    }
}

fun Activity.getEventViewModel(): AppEventViewModel {
    BaseApplication.instance.let {
        return it.getAppViewModelProvider().get(AppEventViewModel::class.java)
    }
}