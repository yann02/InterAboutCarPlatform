package com.hnsh.dialogue.jet.network

import com.hnsh.dialogue.jet.common.base.models.HttpBaseBean
import com.hnsh.dialogue.jet.common.symbols.Constants
import retrofit2.http.*

/**
 * Created with Android Studio.
 * Description:
 * @author: Yingyan Wu
 * @CreateDate: 2020/08/25
 */
interface ApiService {
    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST(Constants.URL_MAIN_MENU_DATA)
    suspend fun searchPlaces(@FieldMap params: Map<String, Any>): HttpBaseBean
}