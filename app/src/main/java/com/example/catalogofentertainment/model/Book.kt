package com.example.catalogofentertainment.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.*
import com.example.catalogofentertainment.model.enums.BookStatus
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Book")
data class Book (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", index = true)
    var id: Long? = null,

    @ColumnInfo(name = "isbn")
    var isbn: String?,

    @ColumnInfo(name = "title")
    var title: String?,

    @ColumnInfo(name = "serieTitle")
    var serieTitle: String?,

    @ColumnInfo(name = "authors")
    var authors: String?,

    @ColumnInfo(name = "publisher")
    var publisher: String?,

    @ColumnInfo(name = "publishedDate")
    var publishedDate: String?,

    @ColumnInfo(name = "description")
    var description: String?,

    @ColumnInfo(name = "pageCount")
    var pageCount: String?,

    @ColumnInfo(name = "imageLinks")
    var imageLinks: ImageLinks?,

    @ColumnInfo(name = "language")
    var language: String?,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB, name = "bitmap")
    var bitmap: Bitmap?,

    @ColumnInfo(name = "status")
    var status: BookStatus,

    @ColumnInfo(name = "isSerie")
    var isSerie: Boolean

) : Parcelable

