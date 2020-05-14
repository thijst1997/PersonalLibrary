package com.example.catalogofentertainment.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.catalogofentertainment.model.Book

class BooksDbRepository(context: Context) {

    private val booksDao: BooksDao

    init {
        val database = BooksRoomDatabase.getDatabase(context)
        booksDao = database!!.booksDao()
    }

    // Book
    fun getAllBooks() : LiveData<List<Book>> {
        return booksDao.getAllBooks()
    }

    suspend fun getBookByISBN(isbn: String) : Book {
        return booksDao.getBookByISBN(isbn)
    }

    suspend fun deleteAllBooks(){
        booksDao.deleteAllBooks()
    }

    suspend fun deleteBook(book: Book) {
        booksDao.deleteBook(book)
    }
    suspend fun insertBook(book: Book) {
        booksDao.insertBook(book)
    }
    suspend fun updateBook(book: Book) {
        booksDao.updateBook(book)
    }



}