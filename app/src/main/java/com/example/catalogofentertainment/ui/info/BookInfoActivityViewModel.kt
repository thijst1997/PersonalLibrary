package com.example.catalogofentertainment.ui.info

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.catalogofentertainment.database.BooksDbRepository
import com.example.catalogofentertainment.model.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookInfoActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val bookDbRepository = BooksDbRepository(application)
    private val mainScope = CoroutineScope(Dispatchers.IO)

    val book = MutableLiveData<Book>()

    fun updateBook(){

        mainScope.launch {
            withContext(Dispatchers.IO) {

                bookDbRepository.updateBook(book.value!!)

            }
        }
    }

}