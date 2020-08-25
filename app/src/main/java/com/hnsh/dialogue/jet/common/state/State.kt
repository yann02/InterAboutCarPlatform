package com.hnsh.dialogue.jet.common.state

import androidx.annotation.StringRes

/**
 * Created with Android Studio.
 * Description:
 * @author: wyy
 * @CreateDate: 2020/6/3 23:01
 */
data class State(var code: StateType, var message: String = "", @StringRes var tip: Int = 0)