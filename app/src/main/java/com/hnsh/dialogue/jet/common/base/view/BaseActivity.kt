package com.hnsh.dialogue.jet.common.base.view

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.hnsh.dialogue.R
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.hnsh.dialogue.jet.common.AppManager
import com.hnsh.dialogue.jet.common.base.viewmodels.BaseViewModel
import com.hnsh.dialogue.jet.common.utils.CommonUtil

/**
 * Created with Android Studio.
 * Description:
 * @author: wyy
 * @CreateDate: 2020/6/3 21:58
 */
abstract class BaseActivity<VM : BaseViewModel<*>, DB : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var mViewModel: VM
    protected lateinit var mDataBinding: DB

    open fun initView() {}

    open fun initData() {}

    open fun reLoad() {}

    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mDataBinding.lifecycleOwner = this
        AppManager.instance.addActivity(this)
        mViewModel = ViewModelProvider(this).get(CommonUtil.getClass(this))
        initView()
        initData()
    }

    val loadService: LoadService<*> by lazy {
        LoadSir.getDefault().register(this) {
            reLoad()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.instance.removeActivity(this)
    }

    private fun initStatusColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
        if (ColorUtils.calculateLuminance(getColor(R.color.white)) >= 0.5) {
            // 设置状态栏中字体的颜色为黑色
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            // 跟随系统
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

}