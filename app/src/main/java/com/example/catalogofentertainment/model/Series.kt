package com.example.catalogofentertainment.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Series (

    var title: String?,
    var books: List<Book>

) : Parcelable