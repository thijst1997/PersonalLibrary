package com.example.catalogofentertainment.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Catalog (
    var books: List<Book>,
    var series: List<Series>
) : Parcelable
