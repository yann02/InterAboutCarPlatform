package com.hnsh.dialogue.jet.modules.main.viewmodel

import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.hnsh.dialogue.MainActivity
import com.hnsh.dialogue.jet.common.base.viewmodels.BaseViewModel
import com.hnsh.dialogue.jet.modules.main.repository.MainRepository

class MainViewModel : BaseViewModel<MainRepository>() {
    lateinit var mActivity: FragmentActivity

    fun getActivity(activity: FragmentActivity) {
        mActivity = activity
    }

    fun intentToDialogueTranslate() {
        Log.d("wyy","intentToDialogueTranslate")
        mActivity.startActivity(Intent(mActivity, MainActivity::class.java))
    }
}