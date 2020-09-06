package org.abanoubmilad.nut


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