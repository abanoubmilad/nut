package org.abanoubmilad.nut

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
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

    var networkFailureCallBack: (() -> Unit)?

    fun disposeISync() {
        networkFailureCallBack = null
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

    fun <R, T> makeNetworkRequestsSequential(
        firstCall: Single<Response<R>>,
        secondCall: (R?) -> Single<Response<T>>,
        onSuccess: (T?) -> Unit,
        onFailure: (ISyncStatus) -> Unit,
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
        onFailure: (ISyncStatus) -> Unit
    ) {
        AppLogger.e(TAG, response.toString())

        onFailure(
            ISyncStatus.build(
                message = response.localizedMessage,
                code = 0
            )
        )
        networkFailureCallBack?.invoke()
    }

    private fun <R> handleNetworkResponse(
        response: Response<R>,
        onSuccess: (R?) -> Unit,
        onFailure: (ISyncStatus) -> Unit
    ) {
        if (response.isSuccessful) {
            onSuccess(response.body())
        } else {
            AppLogger.e(TAG, response.errorBody()?.charStream().toString())
            onFailure(
                ISyncStatus.build(
                    message = (response.body() as? ISyncStatus)?.message,
                    code = response.code()
                )
            )
        }
    }

    fun <R> makeNetworkRequest(
        call: Single<Response<R>>,
        onSuccess: (R?) -> Unit,
        onFailure: (ISyncStatus) -> Unit,
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
        secondCall: Single<Response<T>>,
        secondOnSuccess: (T?) -> Unit,
        onFailure: (ISyncStatus) -> Unit,
        finally: (() -> Unit)? = null
    ) {

        makeParallel(firstCall,
            {
                handleNetworkResponse(it, firstOnSuccess, onFailure)
            },
            secondCall,
            {
                handleNetworkResponse(it, secondOnSuccess, onFailure)
            }, {
                handleNetworkFailure(it, onFailure)
            }
            , finally
        )


    }
}