package com.example.catalogofentertainment.ui.addBook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.catalogofentertainment.api.BooksApiRepository
import com.example.catalogofentertainment.database.BooksDbRepository
import com.example.catalogofentertainment.model.*
import com.example.catalogofentertainment.model.enums.BookStatus
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response

class AddBookActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val bookApiRepository = BooksApiRepository()
    private val bookDbRepository = BooksDbRepository(application)
    private val mainScope = CoroutineScope(Dispatchers.IO)

    val book = MutableLiveData<Book>()
    val serieTitles: LiveData<List<Book>> = bookDbRepository.getAllBooks()
    val error = MutableLiveData<String>()
    val isApi = MutableLiveData<Boolean>()

    init {
        isApi.value = false
        book.value = Book(isbn = "",
            title = "",
            serieTitle = "",
            authors = "",
            publisher = "",
            publishedDate = "",
            description = "",
            language = "",
            pageCount = "",
            imageLinks = ImageLinks(""),
            status = BookStatus.PENDING,
            isSerie = false,
            bitmap = null)

    }

    fun requestBook(isbn: String){

        bookApiRepository.getBook(isbn).enqueue(object : retrofit2.Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {

                    var item = response.body()?.items?.first()

                    if(item != null){

                        isApi.value = true

                        var isbn = item.volumeInfo.industryIdentifiers.first { x -> x.type == "ISBN_13" }.identifier

                        var ownedBook: Book? = null
                        mainScope.launch {

                             ownedBook = bookDbRepository.getBookByISBN(isbn.toString())

                        }

                        if(ownedBook == null){

                              book.value = Book(isbn = isbn,
                                                title = item.volumeInfo.title,
                                                serieTitle = "",
                                                authors = item.volumeInfo.authors.toString().trim('[', ']'),
                                                publisher = item.volumeInfo.publisher,
                                                publishedDate = item.volumeInfo.publishedDate,
                                                description = item.volumeInfo.description,
                                                language = item.volumeInfo.language,
                                                pageCount = item.volumeInfo.pageCount,
                                                imageLinks = item.volumeInfo.imageLinks,
                                                status = BookStatus.PENDING,
                                                isSerie = false,
                                                bitmap = null)

                            error.value = ""
                        }  else { error.value = "Book already in your catalog" }
                    } else {
                        error.value = "Book not found in online database"
                        isApi.value = false
                    }
                } else { error.value = "An error occurred: ${response.errorBody().toString()}" }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                error.value = t.message
            }
        })
    }

    fun addBook(){

        mainScope.launch {
            withContext(Dispatchers.IO) {

                    bookDbRepository.insertBook(book.value!!)

            }
        }
    }

}