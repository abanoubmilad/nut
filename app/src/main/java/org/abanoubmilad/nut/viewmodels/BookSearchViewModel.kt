package org.abanoubmilad.nut.viewmodels

import androidx.lifecycle.MutableLiveData
import org.abanoubmilad.nut.AppLogger
import org.abanoubmilad.nut.BaseViewModel
import org.abanoubmilad.nut.models.ErrorResponse
import org.abanoubmilad.nut.models.VolumesResponse
import org.abanoubmilad.nut.parseAs
import org.abanoubmilad.nut.repositories.BookRepository

class BookSearchViewModel : BaseViewModel() {
    private lateinit var bookRepository: BookRepository
    var volumesResponseLiveData: MutableLiveData<VolumesResponse?> = MutableLiveData()
        private set

    fun init() {
        bookRepository = BookRepository()
        AppLogger.debugMode = true
    }

    fun searchVolumes(keyword: String?, author: String?) {
        makeNetworkRequest(bookRepository.searchVolumes(keyword, author), {
            volumesResponseLiveData.postValue(it)

        }, {
            AppLogger.e("NetworkError", it.code.toString())
            AppLogger.e(
                "NetworkError",
                it.parseAs<ErrorResponse>()?.error?.message.orEmpty()
            )
        })
    }


    fun searchVolumesThreeParallel(
        keyword: String?,
        author: String?
    ) {

        makeNetworkRequestsParallel(
            bookRepository.searchVolumes(keyword, author), {
                volumesResponseLiveData.postValue(it)

            }, {

                AppLogger.e("NetworkError", "first request" + it.code.toString())
                AppLogger.e(
                    "NetworkError",
                    it.parseAs<ErrorResponse>()?.error?.message.orEmpty()
                )

            }, bookRepository.searchVolumes(keyword, author), {
                volumesResponseLiveData.postValue(it)

            }, {
                AppLogger.e("NetworkError", "second request" + it.code.toString())
                AppLogger.e(
                    "NetworkError",
                    it.parseAs<ErrorResponse>()?.error?.message.orEmpty()
                )

            },
            bookRepository.searchVolumes(keyword, author), {
                volumesResponseLiveData.postValue(it)

            }, {
                AppLogger.e("NetworkError", "third request" + it.code.toString())
                AppLogger.e(
                    "NetworkError",
                    it.parseAs<ErrorResponse>()?.error?.message.orEmpty()
                )

            }, {
                AppLogger.e("NetworkError", it.code.toString())
                AppLogger.e(
                    "NetworkError",
                    it.parseAs<ErrorResponse>()?.error?.message.orEmpty()
                )
            })

    }
}