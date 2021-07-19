package io.nandha.userprofiles.model

import io.nandha.userprofiles.model.Repository.Companion.NETWORK_COUNT_PER_PAGE
import io.nandha.userprofiles.model.data.Response
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    /**
     * Get repos ordered by stars.
     */
    @GET("/api/?results=${NETWORK_COUNT_PER_PAGE}&seed=abc")
    suspend fun getUser(@Query("page") page: Int): Response

    companion object {
        private const val BASE_URL = "https://randomuser.me/"

        fun create(): Api {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }
}