package com.example.catalogofentertainment.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BooksApi {

    companion object {
        private const val baseUrl = "https://www.googleapis.com/books/v1/"

        fun createApi(): BooksApiService {

            val okHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()

            val booksApi = Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()

            return booksApi.create(BooksApiService::class.java)

        }


    }

}