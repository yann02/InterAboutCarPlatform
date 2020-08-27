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

    /**
     * @JvmSuppressWildcards @FormUrlEncoded 两注解是为了让kotlin的Any类型在转Java的object类型时能正常转换
     */
    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST(Constants.URL_GET_APK_INFO)
    suspend fun getApkInfo(@FieldMap params: Map<String, Any>): HttpBaseBean
}