package com.hnsh.dialogue.jet.common.base.repositorys

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created with Android Studio.
 * Description:
 * @author: wyy
 * @CreateDate: 2020/6/3 22:51
 */
open class BaseRepository {
    private val mCompositeDisposable by lazy { CompositeDisposable() }

    fun addSubscribe(disposable: Disposable) = mCompositeDisposable.add(disposable)

    fun unSubscribe() = mCompositeDisposable.dispose()
}