package com.example.catalogofentertainment.ui.main.expandableRecyclerView

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.example.catalogofentertainment.R
import com.example.catalogofentertainment.model.Book
import com.example.catalogofentertainment.ui.info.BookInfoActivity
import com.squareup.picasso.Picasso
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.expandable_item_parent.*

class ExpandableParentBook(var context: Context, private val book: Book) : Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {

            tvBookSeriesTitle.text = book.title

            if(book.bitmap != null){
                ivBookCover.setImageBitmap(book.bitmap)
            } else if(!book.imageLinks?.smallThumbnail.isNullOrBlank()){
                Picasso.get().load(book.imageLinks?.smallThumbnail).into(ivBookCover)
            } else {
                ivBookCover.setImageResource(R.drawable.icon_camera)
            }

            ivArrow.setImageResource(0)
            expandableParentLayout.setOnClickListener {
                val intent = Intent(context, BookInfoActivity::class.java)
                intent.putExtra("BOOK", book)
                startActivity(context, intent, Bundle.EMPTY)
            }
        }
    }

    override fun getLayout() = R.layout.expandable_item_parent

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }



}