package com.example.catalogofentertainment.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.example.catalogofentertainment.model.ImageLinks
import com.example.catalogofentertainment.model.IndustryIdentifier
import com.example.catalogofentertainment.model.enums.BookStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.util.*

class TypeConverters {

    private val gson = Gson()


    @TypeConverter
    fun toOrdinal(status: BookStatus): Int {
        return status.ordinal
    }

    @TypeConverter
    fun toBookStatus(ordinal: Int): BookStatus?{
        return BookStatus.values().first { it.ordinal == ordinal }
    }

    @TypeConverter
    fun fromImageLink(imageLinks: ImageLinks?): String {
        return imageLinks?.smallThumbnail.toString()
    }

    @TypeConverter
    fun toImageLink(value: String): ImageLinks{
        return ImageLinks(value)
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 50 , stream)
        stream.close()
        return stream.toByteArray()

    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }

}