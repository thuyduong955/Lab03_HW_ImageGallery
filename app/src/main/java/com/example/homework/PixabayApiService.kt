package com.example.homework

import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {
    // Trỏ vào gốc của Base URL (https://pixabay.com/api/)
    @GET("./")
    suspend fun searchImages(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): PixabayResponse
}