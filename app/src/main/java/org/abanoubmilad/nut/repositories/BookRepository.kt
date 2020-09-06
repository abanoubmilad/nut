package org.abanoubmilad.nut.repositories

import io.reactivex.Single
import org.abanoubmilad.nut.ApiBuilder
import org.abanoubmilad.nut.apis.BookSearchService
import org.abanoubmilad.nut.models.VolumesResponse
import retrofit2.Response

class BookRepository {
    private var bookSearchService: BookSearchService
    fun searchVolumes(
        keyword: String?,
        author: String?
    ): Single<Response<VolumesResponse>> {
        return bookSearchService.searchVolumes(keyword, author)
    }

    companion object {
        private const val BOOK_SEARCH_SERVICE_BASE_URL = "https://www.googleapis.com/"
    }

    init {
        bookSearchService =
            ApiBuilder(enableDebug = true).createRXApi(
                BookSearchService::class.java,
                BOOK_SEARCH_SERVICE_BASE_URL
            )
    }
}