package com.example.catalogofentertainment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.catalogofentertainment.model.Book

@Database(entities = [Book::class], version = 12, exportSchema = false)
@androidx.room.TypeConverters(TypeConverters::class)
abstract class BooksRoomDatabase : RoomDatabase()  {

    abstract fun booksDao(): BooksDao

    companion object {
        private const val DATABASE_NAME = "CATALOG_OF_ENTERTAINMENT_DATABASE"

        @Volatile
        private var booksRoomDatabaseInstance: BooksRoomDatabase? = null

        fun getDatabase(context: Context) : BooksRoomDatabase? {
            if (booksRoomDatabaseInstance == null) {
                synchronized(BooksRoomDatabase::class.java) {
                    if (booksRoomDatabaseInstance == null) {
                        booksRoomDatabaseInstance =
                            Room.databaseBuilder(context.applicationContext,
                                BooksRoomDatabase::class.java,
                                DATABASE_NAME
                            ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return booksRoomDatabaseInstance
        }

    }




}