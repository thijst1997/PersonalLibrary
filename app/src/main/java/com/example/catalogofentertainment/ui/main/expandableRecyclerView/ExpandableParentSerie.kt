package com.example.catalogofentertainment.ui.main.expandableRecyclerView

import com.example.catalogofentertainment.R
import com.example.catalogofentertainment.model.Series
import com.squareup.picasso.Picasso
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.expandable_item_parent.*

class ExpandableParentSerie(private val serie:Series) : Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {

            tvBookSeriesTitle.text = fitText(serie.title.toString())

            if(serie.books.first().bitmap != null) {
                ivBookCover.setImageBitmap(serie.books.first().bitmap)
            } else if(!serie.books.first().imageLinks?.smallThumbnail.isNullOrBlank()){
                Picasso.get().load(serie.books.first().imageLinks?.smallThumbnail).into(ivBookCover)
            } else  {
                ivBookCover.setImageResource(R.drawable.icon_camera)
            }

            expandableParentLayout.setOnClickListener {

                if (expandableGroup.isExpanded){
                    ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_yellow_24dp)
                } else {
                    ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_yellow_24dp)
                }

                expandableGroup.onToggleExpanded()

            }
        }
    }

    override fun getLayout() = R.layout.expandable_item_parent

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    private fun fitText(string: String): String{
        return if(string.length > 27){
            string.substring(0,27) + "..."
        } else {
            string
        }
    }

}