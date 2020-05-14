package com.example.catalogofentertainment.ui.main.expandableRecyclerView

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.catalogofentertainment.R
import com.example.catalogofentertainment.model.Book
import com.example.catalogofentertainment.model.Catalog
import com.example.catalogofentertainment.ui.info.BookInfoActivity
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.expandable_item_child.*

class ExpandableChild(var context: Context, private val book: Book) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {

            tvBookTitle.text = book.title

            expandableChildCard.setOnClickListener {
                val intent = Intent(context, BookInfoActivity::class.java)
                intent.putExtra("BOOK", book)
                ContextCompat.startActivity(context, intent, Bundle.EMPTY)
            }

        }
    }

    override fun getLayout(): Int =
        R.layout.expandable_item_child

    override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount / 3

}