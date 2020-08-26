package com.hnsh.dialogue.jet.common.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDex
import com.dosmono.library.iflytek.Iflytek.initIflytek
import com.dosmono.universal.app.DeviceInfo
import com.dosmono.universal.app.Framework.initFramework
import com.dosmono.universal.common.Constant
import com.hnsh.dialogue.jet.common.callback.EmptyCallBack
import com.hnsh.dialogue.jet.common.callback.ErrorCallBack
import com.hnsh.dialogue.jet.common.callback.LoadingCallBack
import com.hnsh.dialogue.utils.UIUtils
import com.hnsh.dialogue.utils.UUIDUtils
//import androidx.startup.AppInitializer
import com.kingja.loadsir.core.LoadSir
//import com.wjx.android.weather.common.startup.WeatherStartUp
//import com.wjx.android.weather.common.util.SPreference
import java.lang.reflect.ParameterizedType

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @CreateDate: 2020/6/3 21:57
 */
open class BaseApplication  : Application(), ViewModelStoreOwner {
    lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    companion object {
        lateinit var instance : BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
//        SPreference.setContext(this)
        initLoadSir()
        mAppViewModelStore = ViewModelStore()
//        AppInitializer.getInstance(instance).initializeComponent(WeatherStartUp::class.java)
        initFramework()
        UIUtils.setContext(this)
    }

    private fun initLoadSir() {
        LoadSir.beginBuilder()
            .addCallback(ErrorCallBack())
            .addCallback(LoadingCallBack())
            .addCallback(EmptyCallBack())
            .commit()
    }

    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }

    @Suppress("UNCHECKED_CAST")
    fun <VM> getVmClazz(obj: Any): VM {
        return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
    }

    private fun initFramework() {
        initIflytek(applicationContext)
        val deviceInfo = DeviceInfo()
        deviceInfo.setPushUrl(Constant.MPUSH_URL)
                .setPushAccount(UUIDUtils.initUUID(this))
                .setPushDevid("543142001000076") //.setPushDevid()CommonUtil.getDeviceId()
                .setPushTags("push-" + System.currentTimeMillis())
                .setSupportBpush(true)
                .setOnlyMarkType(Constant.ID_TYPE_SN)
                .setIdentification("543142001000076") //.setIdentification(CommonUtil.getDeviceId())
                .setDevType(Constant.DEV_PARTNER).isAutoUpdateConfig = true
        initFramework(this, deviceInfo)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}