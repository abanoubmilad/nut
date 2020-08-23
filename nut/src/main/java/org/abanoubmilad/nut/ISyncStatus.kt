package org.abanoubmilad.nut

import com.google.gson.annotations.SerializedName


/*
 * *
 *  * Created by Abanoub Milad Nassief Hanna
 *  * on 5/1/20 11:05 PM
 *  * Last modified 5/1/20 11:05 PM
 *
 */

interface ISyncStatus {
    var message: String?
    var code: Int

    companion object {
        fun build(message: String? = null, code: Int = 0): ISyncStatus {
            return object : ISyncStatus {
                override var message: String? = message
                override var code: Int = code
            }
        }
    }
}

open class NetworkResponseStatus : ISyncStatus {
    @SerializedName("message")
    override var message: String? = null
    @SerializedName("code")
    override var code = 0
}