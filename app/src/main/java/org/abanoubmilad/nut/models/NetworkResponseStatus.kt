package org.abanoubmilad.nut.models

import com.google.gson.annotations.SerializedName
import org.abanoubmilad.nut.ISyncStatus


/*
 * *
 *  * Created by Abanoub Milad Nassief Hanna
 *  * on 5/1/20 11:05 PM
 *  * Last modified 5/1/20 11:05 PM
 *
 */

open class NetworkResponseStatus : ISyncStatus {
    @SerializedName("error")
    var error: ErrorBody? = null
    override var message
        get() = error?.message
        set(value) {
            error?.message = value
        }
    override var code
        get() = error?.code ?: 0
        set(value) {
            error?.code = value
        }
}

class ErrorBody {
    @SerializedName("error")
    var message: String? = null

    @SerializedName("code")
    var code = 0
}