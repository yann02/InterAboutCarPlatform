package com.hnsh.dialogue.jet.modules.main.view

import android.os.Bundle
import com.hnsh.dialogue.R
import com.hnsh.dialogue.databinding.FragmentMainBinding
import com.hnsh.dialogue.jet.common.base.view.BaseLifeCycleFragment
import com.hnsh.dialogue.jet.modules.main.viewmodel.MainViewModel

/**
 * 首页
 * Create by wyy on 2020/08/26
 */
class MainFragment : BaseLifeCycleFragment<MainViewModel, FragmentMainBinding>() {
    override fun getLayoutId() = R.layout.fragment_main

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.mActivity = requireActivity()
        mDataBinding.main = mViewModel
        showSuccess()
    }

    override fun initView() {
        super.initView()
    }
}
