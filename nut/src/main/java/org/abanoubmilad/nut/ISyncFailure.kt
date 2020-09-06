package org.abanoubmilad.nut

import com.google.gson.Gson
import java.io.Reader


/*
 * *
 *  * Created by Abanoub Milad Nassief Hanna
 *  * on 5/1/20 11:05 PM
 *  * Last modified 5/1/20 11:05 PM
 *
 */

interface ISyncFailure {
    var errorBody: Reader?
    var code: Int

    companion object {

        const val TAG = "ISync_failure_nut_module"

        fun build(errorBody: Reader? = null, code: Int = 0): ISyncFailure {
            return object : ISyncFailure {
                override var errorBody = errorBody
                override var code = code
            }
        }
    }
}

inline fun <reified ErrorModel> ISyncFailure.parseAs(): ErrorModel? {
    if (errorBody == null) return null
    return try {
        Gson().fromJson(
            errorBody,
            ErrorModel::class.java
        )
    } catch (e: Exception) {
        AppLogger.e(ISyncFailure.TAG, errorBody.toString())

        null
    }
}