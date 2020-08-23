package org.abanoubmilad.nut

import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/*
 * *
 *  * Created by Abanoub Milad Nassief Hanna
 *  * on 5/1/20 11:05 PM
 *  * Last modified 5/1/20 11:05 PM
 *
 */
open class BaseViewModel : ViewModel(), ISync, IObserve {
    companion object {
        var appNetworkFailureCallBack: (() -> Unit)? = null
    }

    override var networkFailureCallBack = appNetworkFailureCallBack

    override val disposable = CompositeDisposable()

    override val observablesMap by lazy {
        hashMapOf<ObservableField<*>, Observable.OnPropertyChangedCallback>()
    }

    private val _message =
        SingleLiveEvent<@IdRes Int>()
    val message: LiveData<Int> = _message

    private val _loading =
        MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun showLoading(show: Boolean) {
        _loading.postValue(show)
    }

    fun showLoading() {
        _loading.postValue(true)
    }

    fun hideLoading() {
        _loading.postValue(false)
    }

    fun fireMessage(@IdRes msg: Int) {
        _message.postValue(msg)
    }

    fun visibleOrInvisible(visible: Boolean): Int {
        return if (visible) View.VISIBLE else View.INVISIBLE
    }

    fun visibleOrGone(visible: Boolean): Int {
        return if (visible) View.VISIBLE else View.GONE
    }

    @CallSuper
    override fun onCleared() {
        disposeISync()
        disposeIObserve()
    }


}