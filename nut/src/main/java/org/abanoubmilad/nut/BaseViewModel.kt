package org.abanoubmilad.nut

import androidx.annotation.CallSuper
import androidx.databinding.Observable
import androidx.databinding.ObservableField
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

    override val disposable = CompositeDisposable()

    override val observablesMap by lazy {
        hashMapOf<ObservableField<*>, Observable.OnPropertyChangedCallback>()
    }

    @CallSuper
    override fun onCleared() {
        disposeISync()
        disposeIObserve()
    }


}