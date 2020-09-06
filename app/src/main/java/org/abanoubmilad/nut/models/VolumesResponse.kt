package org.abanoubmilad.nut.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VolumesResponse : NetworkResponseStatus() {
    @SerializedName("kind")
    @Expose
    val kind: String? = null

    @SerializedName("items")
    @Expose
    var items: List<Volume>? = null

    @SerializedName("totalItems")
    @Expose
    var totalItems = 0

}