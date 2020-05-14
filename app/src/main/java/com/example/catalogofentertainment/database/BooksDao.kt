package com.example.catalogofentertainment.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.catalogofentertainment.model.Book

@Dao
interface BooksDao {

    @Query("SELECT * FROM Book")
    fun getAllBooks(): LiveData<List<Book>>

    @Insert
    suspend fun insertBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Query("DELETE FROM Book")
    suspend fun deleteAllBooks()

    @Query("SELECT * FROM Book WHERE isbn == :isbn")
    suspend fun getBookByISBN(isbn: String): Book



}