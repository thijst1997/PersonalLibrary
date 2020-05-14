package com.example.catalogofentertainment.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.catalogofentertainment.database.BooksDbRepository
import com.example.catalogofentertainment.model.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val bookDbRepository = BooksDbRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.IO)

    val books: LiveData<List<Book>> = bookDbRepository.getAllBooks()

    fun deleteAllBooks(){

        mainScope.launch {
            withContext(Dispatchers.IO) {

                bookDbRepository.deleteAllBooks()

            }
        }
    }

    fun deleteBook(book: Book){

        mainScope.launch {
            withContext(Dispatchers.IO) {

                bookDbRepository.deleteBook(book)

            }
        }
    }
}