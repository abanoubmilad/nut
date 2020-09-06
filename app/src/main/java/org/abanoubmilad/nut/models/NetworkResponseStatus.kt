package org.abanoubmilad.nut.models

import com.google.gson.annotations.SerializedName


/*
 * *
 *  * Created by Abanoub Milad Nassief Hanna
 *  * on 5/1/20 11:05 PM
 *  * Last modified 5/1/20 11:05 PM
 *
 */


data class Error(

    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("errors") val errors: List<Errors>
)

data class Errors(

    @SerializedName("message") val message: String,
    @SerializedName("domain") val domain: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("location") val location: String,
    @SerializedName("locationType") val locationType: String
)


data class ErrorResponse(

    @SerializedName("error") val error: Error
)