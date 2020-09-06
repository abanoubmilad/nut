package org.abanoubmilad.nut.apis

import io.reactivex.Single
import org.abanoubmilad.nut.models.VolumesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookSearchService {
    @GET("/books/v1/volumes")
    fun searchVolumes(
        @Query("q") query: String?,
        @Query("inauthor") author: String?
    ): Single<Response<VolumesResponse>>
}