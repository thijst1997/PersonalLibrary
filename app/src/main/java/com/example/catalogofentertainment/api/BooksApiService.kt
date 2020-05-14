package com.example.catalogofentertainment.api

import com.example.catalogofentertainment.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApiService {

    @GET("volumes?q=isbn:9780385736121&key=AIzaSyBQ7xNDio1Bma6bI99wNn6SLJizBII8YzY" )
    fun getDefaultBook(): Call<ApiResponse>

    @GET("volumes" )
    fun getBook(@Query("q") isbn: String, @Query("key") key: String): Call<ApiResponse>

}