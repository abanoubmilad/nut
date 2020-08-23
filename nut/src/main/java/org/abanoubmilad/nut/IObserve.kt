package org.abanoubmilad.nut

import androidx.databinding.Observable
import androidx.databinding.ObservableField

/*
 * *
 *  * Created by Abanoub Milad Nassief Hanna
 *  * on 5/1/20 11:05 PM
 *  * Last modified 5/1/20 11:05 PM
 *
 */
interface IObserve {
    val observablesMap: HashMap<ObservableField<*>, Observable.OnPropertyChangedCallback>


    fun <K> observe(observable: ObservableField<K>, callback: (K) -> Unit) {
        val propertyCallback = object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable, i: Int) {
                (observable as? ObservableField<K>)?.get()?.let(callback)
            }
        }
        observable.addOnPropertyChangedCallback(propertyCallback)
        observablesMap[observable] = propertyCallback
    }

    fun <K> observeAndFetch(observable: ObservableField<K>, callback: (K) -> Unit) {
        observe(observable, callback)
        (observable as? ObservableField<K>)?.get()?.let(callback)
    }

    fun disposeIObserve() {
        for ((observable, callback) in observablesMap) {
            observable.removeOnPropertyChangedCallback(callback)
        }


    }

}