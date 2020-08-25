package com.hnsh.dialogue.jet.main.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dosmono.universal.utils.MD5.md5
import com.hnsh.dialogue.jet.common.base.models.HttpBaseBean
import com.hnsh.dialogue.jet.common.base.repositorys.ApiRepository
import com.hnsh.dialogue.jet.common.state.State
import com.hnsh.dialogue.jet.common.symbols.Constants
import java.util.*

/**
 * Created with Android Studio.
 * Description:
 * @author: wyy
 * @CreateDate: 2020/6/6 10:43
 */
class SearchPlaceRepository(var loadState: MutableLiveData<State>) : ApiRepository() {
    suspend fun searchPlaces(): HttpBaseBean {
        val mapParam: MutableMap<String, Any> = LinkedHashMap()
        val salt = System.currentTimeMillis()
        val serial = "112041805000080"
        var sign = serial + salt + Constants.DEVICE_SECRET_KEY
        sign = md5(sign)!!
        mapParam["sno"] = serial
        mapParam["salt"] = salt
        mapParam["sign"] = sign
        for (item in mapParam) {
            Log.d("wyy", "item.${item.key}=${item.value}")
        }
        return apiService.searchPlaces(mapParam)
    }
}