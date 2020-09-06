package org.abanoubmilad.nut.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VolumeInfo {
    @SerializedName("title")
    @Expose
    val title: String? = null

    @SerializedName("authors")
    @Expose
    val authors: List<String>? = null

    @SerializedName("publisher")
    @Expose
    val publisher: String? = null

    @SerializedName("publishedDate")
    @Expose
    val publishedDate: String? = null

    @SerializedName("description")
    @Expose
    val description: String? = null

    @SerializedName("pageCount")
    @Expose
    val pageCount = 0

    @SerializedName("printType")
    @Expose
    val printType: String? = null

    @SerializedName("imageLinks")
    @Expose
    val imageLinks: VolumeImageLinks? = null

}