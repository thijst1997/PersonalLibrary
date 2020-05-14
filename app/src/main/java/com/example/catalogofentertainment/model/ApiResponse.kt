package com.example.catalogofentertainment.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.catalogofentertainment.model.enums.BookStatus
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ApiResponse (

    @SerializedName("items")
    var items: List<Item>

)

data class Item(

    @SerializedName("id")
    var googleId: String,

    @SerializedName("volumeInfo")
    var volumeInfo: VolumeInfo

)

@Parcelize
data class VolumeInfo (

    @SerializedName("title")
    var title: String,

    @SerializedName("authors")
    var authors: List<String>,

    @SerializedName("publisher")
    var publisher: String,

    @SerializedName("publishedDate")
    var publishedDate: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("industryIdentifiers")
    var industryIdentifiers: List<IndustryIdentifier>,

    @SerializedName("pageCount")
    var pageCount: String,

    @SerializedName("imageLinks")
    var imageLinks: ImageLinks,

    @SerializedName("language")
    var language: String

) : Parcelable

@Parcelize
data class ImageLinks(

    @SerializedName("smallThumbnail")
    var smallThumbnail: String

) : Parcelable

@Parcelize
data class IndustryIdentifier(

    @SerializedName("type")
    var type: String,

    @SerializedName("identifier")
    var identifier: String?

)  : Parcelable
