package com.hnsh.dialogue.jet.common.base.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.hnsh.dialogue.R
import com.hnsh.dialogue.jet.common.base.viewmodels.BaseViewModel
import com.hnsh.dialogue.jet.common.utils.CommonUtil

/**
 * Created with Android Studio.
 * Description:
 * @author: wyy
 * @CreateDate: 2020/6/3 22:47
 */
abstract class BaseFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> : Fragment() {
    protected lateinit var mViewModel: VM

    protected lateinit var mDataBinding: DB

    protected lateinit var loadService: LoadService<*>

    open fun initView() {}

    open fun initData() {}

    open fun reLoad() = initData()

    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mDataBinding.lifecycleOwner = this
        loadService = LoadSir.getDefault().register(mDataBinding.root) { reLoad() }
        return loadService.loadLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel = ViewModelProvider(this).get(CommonUtil.getClass(this))
        initView()
        initData()
        initStatusBarColor()
    }

    private fun initStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(
                requireActivity(),
                R.color.white
            )
        }
        if (ColorUtils.calculateLuminance(requireContext().getColor(R.color.white)) >= 0.5) {
            // 设置状态栏中字体的颜色为黑色
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            // 跟随系统
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}