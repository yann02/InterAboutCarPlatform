package com.hnsh.dialogue.jet.common.callback

import com.kingja.loadsir.callback.Callback
import com.hnsh.dialogue.R

/**
 * Created with Android Studio.
 * Description:
 * @author: yingyan wu
 * @date: 2020/06/03
 * Time: 14:29
 */
class ErrorCallBack : Callback() {
    override fun onCreateView(): Int = R.layout.layout_error
}