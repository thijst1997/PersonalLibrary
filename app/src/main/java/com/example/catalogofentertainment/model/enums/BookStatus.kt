package com.example.catalogofentertainment.model.enums

enum class BookStatus(val string: String) {
    PENDING("Pending"),
    IN_PROGRESS("In progress"),
    COMPLETED("Completed");

    override fun toString(): String {
        return string
    }

}