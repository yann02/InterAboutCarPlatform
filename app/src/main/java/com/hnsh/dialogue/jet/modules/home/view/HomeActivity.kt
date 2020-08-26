package com.hnsh.dialogue.jet.modules.home.view

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hnsh.dialogue.BuildConfig
import com.hnsh.dialogue.R
import com.hnsh.dialogue.databinding.ActivityHomeBinding
import com.hnsh.dialogue.jet.common.base.view.BaseLifeCycleActivity
import com.hnsh.dialogue.jet.common.requestByPermissionOnActivity
import com.hnsh.dialogue.jet.modules.home.viewmodel.HomeViewModel
import com.hnsh.dialogue.jet.modules.main.view.MainFragment
import com.hnsh.dialogue.jet.modules.mine.view.MineFragment


class HomeActivity : BaseLifeCycleActivity<HomeViewModel, ActivityHomeBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestByPermissionOnActivity({ mViewModel.searchPlaces("") }, this, listOf(Manifest.permission.INTERNET))
        mViewModel.getApkInfo()
        mDataBinding.oneBottomLayout.setFragmentManager(supportFragmentManager, mDataBinding.mainFragment)
        mDataBinding.oneBottomLayout.addFragment(R.id.tab1, MainFragment())
        mDataBinding.oneBottomLayout.addFragment(R.id.tab2, MainFragment())
        mDataBinding.oneBottomLayout.addFragment(R.id.tab3, MainFragment())
        mDataBinding.oneBottomLayout.addFragment(R.id.tab4, MineFragment())
        mDataBinding.oneBottomLayout.setOnItemSelectedListener { _, position ->
            when (position) {
                1, 2 -> Toast.makeText(HomeActivity@ this, "暂未开放", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_home

    override fun initDataObserver() {
        super.initDataObserver()
        mViewModel.mSearchPlacesData.observe(this, Observer {
            Log.d("wyy", "mSearchPlacesData.bodyContent=${it?.bodyContent}")
//            mDataBinding.content = it
        })
        mViewModel.mHttpBaseBean.observe(this, Observer {
            Log.d("wyy", "mHttpBaseBean.bodyContent=${it?.bodyContent}")
        })

    }
}
