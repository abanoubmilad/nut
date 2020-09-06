package org.abanoubmilad.nut.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Volume {
    @SerializedName("kind")
    @Expose
    val kind: String? = null

    @SerializedName("id")
    @Expose
    val id: String? = null

    @SerializedName("etag")
    @Expose
    val etag: String? = null

    @SerializedName("selfLink")
    @Expose
    val selfLink: String? = null

    @SerializedName("volumeInfo")
    @Expose
    var volumeInfo: VolumeInfo? = null

}