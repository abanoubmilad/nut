package org.abanoubmilad.nut

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
 * *
 *  * Created by Abanoub Milad Nassief Hanna
 *  * on 5/1/20 11:05 PM
 *  * Last modified 5/1/20 11:05 PM
 *
 */
open class ApiBuilder(
    private val enableDebug: Boolean = false,
    private val debugLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
    private val headers: HashMap<String, String> = hashMapOf(),
    private val connectTimeout: Int = 30,
    private val readTimeout: Int = 30,
    private val writeTimeout: Int = 30,
    private val serializeNulls: Boolean = false
) {

    /**
     * Network interceptor
     */
    private val interceptor by lazy {
        { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
            request.apply {
                headers.forEach { entry ->
                    addHeader(entry.key, entry.value)
                }
            }
            chain.proceed(request.build())
        }
    }

    /**
     * Client initialization
     */
    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .apply {
                if (enableDebug)
                    addInterceptor(HttpLoggingInterceptor().apply { level = debugLevel })
            }
            .connectTimeout(connectTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(readTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(writeTimeout.toLong(), TimeUnit.SECONDS)
            .build()
    }

    /**
     * Instances for network builder and retrofit initialization
     */

    fun createRXRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .apply {
                if (serializeNulls)
                    addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder().serializeNulls().create()
                        )
                    )
                else
                    addConverterFactory(GsonConverterFactory.create())

            }
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    fun <S> createRXApi(apiInterfaceClass: Class<S>, baseUrl: String): S {
        return createRXRetrofit(baseUrl)
            .create(apiInterfaceClass)
    }


}