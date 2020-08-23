package org.abanoubmilad.nut

import android.util.Log

/*
 * *
 *  * Created by Abanoub Milad Nassief Hanna
 *  * on 5/1/20 11:05 PM
 *  * Last modified 5/1/20 11:05 PM
 *
 */
class AppLogger {
    companion object {
        var debugMode = false
        /**
         * Send a [.DEBUG] log message.
         * @param tag Used to identify the source of a log message.  It usually identifies
         * the class or activity where the log call occurs.
         * @param msg The message you would like logged.
         */
        fun d(tag: String, msg: String) {
            if (debugMode)
                Log.d(tag, msg)
        }

        /**
         * Send a [.DEBUG] log message and log the exception.
         * @param tag Used to identify the source of a log message.  It usually identifies
         * the class or activity where the log call occurs.
         * @param msg The message you would like logged.
         * @param tr An exception to log
         */
        fun d(tag: String, msg: String, tr: Throwable?) {
            if (debugMode)
                Log.d(
                    tag, msg, tr
                )
        }

        /**
         * Send an [.ERROR] log message.
         * @param tag Used to identify the source of a log message.  It usually identifies
         * the class or activity where the log call occurs.
         * @param msg The message you would like logged.
         */
        fun e(tag: String, msg: String) {
            if (debugMode)
                Log.e(tag, msg)
        }

        /**
         * Send a [.ERROR] log message and log the exception.
         * @param tag Used to identify the source of a log message.  It usually identifies
         * the class or activity where the log call occurs.
         * @param msg The message you would like logged.
         * @param tr An exception to log
         */
        fun e(tag: String, msg: String, tr: Throwable?) {
            if (debugMode)
                Log.e(
                    tag, msg, tr
                )
        }
    }
}