package org.abanoubmilad.nut

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/*
 * *
 *  * Created by Abanoub Milad Nassief Hanna
 *  * on 5/1/20 11:05 PM
 *  * Last modified 5/1/20 11:05 PM
 *
 */
interface ISync {

    companion object {
        const val TAG = "ISync_nut_module"
    }

    val disposable: CompositeDisposable

    fun onNetworkFailure() {}

    fun disposeISync() {
        disposable.dispose()
    }


    fun <R> make(
        call: Single<R>,
        onSuccess: (R) -> Unit,
        onFailure: (Throwable) -> Unit,
        finally: (() -> Unit)? = null
    ) {

        disposable.add(
            call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess.invoke(it)
                    finally?.invoke()
                }, {
                    onFailure.invoke(it)
                    finally?.invoke()
                })
        )

    }

    fun <R, T> makeParallel(
        firstCall: Single<R>,
        firstOnSuccess: (R) -> Unit,
        secondCall: Single<T>,
        secondOnSuccess: (T) -> Unit,
        onFailure: (Throwable) -> Unit,
        finally: (() -> Unit)? = null
    ) {

        disposable.add(
            Observable
                .zip(
                    firstCall.toObservable().subscribeOn(Schedulers.io()),
                    secondCall.toObservable().subscribeOn(Schedulers.io()),
                    BiFunction<R, T, Pair<R, T>> { firstResponse, SecondResponse ->
                        // here we get both the results at a time.
                        return@BiFunction Pair(firstResponse, SecondResponse)
                    })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    firstOnSuccess.invoke(it.first)
                    secondOnSuccess.invoke(it.second)
                    finally?.invoke()
                }, {
                    onFailure.invoke(it)
                    finally?.invoke()
                })
        )

    }

    fun <R, T, V> makeParallel(
        firstCall: Single<R>,
        firstOnSuccess: (R) -> Unit,
        secondCall: Single<T>,
        secondOnSuccess: (T) -> Unit,
        thirdCall: Single<V>,
        thirdOnSuccess: (V) -> Unit,
        onFailure: (Throwable) -> Unit,
        finally: (() -> Unit)? = null
    ) {

        disposable.add(
            Observable
                .zip(
                    firstCall.toObservable().subscribeOn(Schedulers.io()),
                    secondCall.toObservable().subscribeOn(Schedulers.io()),
                    thirdCall.toObservable().subscribeOn(Schedulers.io()),
                    Function3<R, T, V, Triple<R, T, V>> { firstResponse, SecondResponse, thirdResponse ->
                        // here we get the results at a time.
                        return@Function3 Triple(firstResponse, SecondResponse, thirdResponse)
                    })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    firstOnSuccess.invoke(it.first)
                    secondOnSuccess.invoke(it.second)
                    thirdOnSuccess.invoke(it.third)
                    finally?.invoke()
                }, {
                    onFailure.invoke(it)
                    finally?.invoke()
                })
        )

    }

    fun <R, T> makeNetworkRequestsSequential(
        firstCall: Single<Response<R>>,
        secondCall: (R?) -> Single<Response<T>>,
        onSuccess: (T?) -> Unit,
        onFailure: (ISyncFailure) -> Unit,
        finally: (() -> Unit)? = null
    ) {

        disposable.add(
            firstCall
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ firstResponse ->
                    handleNetworkResponse(firstResponse, { response ->
                        disposable.add(
                            secondCall.invoke(response)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ secondResponse ->
                                    handleNetworkResponse(secondResponse, onSuccess, onFailure)
                                    finally?.invoke()
                                }, {
                                    handleNetworkFailure(it, onFailure)
                                    finally?.invoke()
                                })
                        )
                    }, { networkResponseStatus ->
                        onFailure.invoke(networkResponseStatus)
                        finally?.invoke()
                    })
                }, {
                    handleNetworkFailure(it, onFailure)
                    finally?.invoke()
                })
        )
    }

    private fun handleNetworkFailure(
        response: Throwable,
        onFailure: (ISyncFailure) -> Unit
    ) {
        AppLogger.e(TAG, response.toString())

        onFailure(
            ISyncFailure.build(
                code = 0
            )
        )
        onNetworkFailure()
    }

    private fun <R> handleNetworkResponse(
        response: Response<R>,
        onSuccess: (R?) -> Unit,
        onFailure: (ISyncFailure) -> Unit
    ) {
        if (response.isSuccessful) {
            onSuccess(response.body())
        } else {
            AppLogger.e(TAG, response.errorBody()?.charStream().toString())
            onFailure(
                ISyncFailure.build(
                    errorBody = response.errorBody()?.charStream(),
                    code = response.code()
                )
            )
        }
    }

    fun <R> makeNetworkRequest(
        call: Single<Response<R>>,
        onSuccess: (R?) -> Unit,
        onFailure: (ISyncFailure) -> Unit,
        finally: (() -> Unit)? = null
    ) {
        make(call,
            {
                handleNetworkResponse(it, onSuccess, onFailure)
            }, {
                handleNetworkFailure(it, onFailure)
            }, finally
        )
    }


    fun <R, T> makeNetworkRequestsParallel(
        firstCall: Single<Response<R>>,
        firstOnSuccess: (R?) -> Unit,
        firstOnFailure: (ISyncFailure) -> Unit,
        secondCall: Single<Response<T>>,
        secondOnSuccess: (T?) -> Unit,
        secondOnFailure: (ISyncFailure) -> Unit,
        onFailure: (ISyncFailure) -> Unit,
        finally: (() -> Unit)? = null
    ) {

        makeParallel(firstCall,
            {
                handleNetworkResponse(it, firstOnSuccess, firstOnFailure)
            },
            secondCall,
            {
                handleNetworkResponse(it, secondOnSuccess, secondOnFailure)
            }, {
                handleNetworkFailure(it, onFailure)
            }
            , finally
        )


    }

    fun <R, T> makeNetworkRequestsParallel(
        firstCall: Single<Response<R>>,
        firstOnSuccess: (R?) -> Unit,
        firstOnFailure: (ISyncFailure) -> Unit,
        secondCall: Single<Response<T>>,
        secondOnSuccess: (T?) -> Unit,
        secondOnFailure: (ISyncFailure) -> Unit,
        thirdCall: Single<Response<T>>,
        thirdOnSuccess: (T?) -> Unit,
        thirdOnFailure: (ISyncFailure) -> Unit,
        onFailure: (ISyncFailure) -> Unit,
        finally: (() -> Unit)? = null
    ) {

        makeParallel(firstCall,
            {
                handleNetworkResponse(it, firstOnSuccess, firstOnFailure)
            },
            secondCall,
            {
                handleNetworkResponse(it, secondOnSuccess, secondOnFailure)
            },
            thirdCall,
            {
                handleNetworkResponse(it, thirdOnSuccess, thirdOnFailure)
            }, {
                handleNetworkFailure(it, onFailure)
            }
            , finally
        )


    }
}