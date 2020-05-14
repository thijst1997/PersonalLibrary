package com.example.catalogofentertainment.api

private const val API_KEY = "KEY_IN_ROOT"

class BooksApiRepository {

    private val booksApi: BooksApiService = BooksApi.createApi()

    fun getBook(isbn: String) = booksApi.getBook("isbn:$isbn", API_KEY)
    fun getDefaultBook() = booksApi.getDefaultBook()


}