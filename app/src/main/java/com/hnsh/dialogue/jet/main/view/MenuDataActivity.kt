package com.hnsh.dialogue.jet.main.view

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.hnsh.dialogue.R
import com.hnsh.dialogue.databinding.ActivityMenuDataBinding
import com.hnsh.dialogue.jet.common.base.view.BaseLifeCycleActivity
import com.hnsh.dialogue.jet.common.requestByPermissionOnActivity
import com.hnsh.dialogue.jet.main.viewmodel.SearchPlaceViewModel

class MenuDataActivity : BaseLifeCycleActivity<SearchPlaceViewModel, ActivityMenuDataBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestByPermissionOnActivity({ mViewModel.searchPlaces("") }, this, listOf(Manifest.permission.INTERNET))
    }

    override fun getLayoutId() = R.layout.activity_menu_data

    override fun initDataObserver() {
        super.initDataObserver()
        mViewModel.mSearchPlacesData.observe(this, Observer {
            Log.d("wyy", "it.bodyContent=${it?.bodyContent}")
            mDataBinding.content = it
        })
    }
}
