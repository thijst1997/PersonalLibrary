package com.example.catalogofentertainment.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogofentertainment.ui.main.expandableRecyclerView.ExpandableChild
import com.example.catalogofentertainment.ui.main.expandableRecyclerView.ExpandableParentBook
import com.example.catalogofentertainment.R
import com.example.catalogofentertainment.model.Book
import com.example.catalogofentertainment.model.Series
import com.example.catalogofentertainment.ui.addBook.AddBookActivity
import com.example.catalogofentertainment.ui.main.expandableRecyclerView.ExpandableParentSerie
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception

const val ADD_BOOK_REQUEST_CODE = 100

class MainActivity : AppCompatActivity() {

    private val catalog = mutableListOf<Any>()
    private val groupAdapter = GroupAdapter<ViewHolder>()
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initViews()
        initViewModel()

    }

    private fun initViews() {

        rvExpandable.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rvExpandable.adapter = groupAdapter
        createItemTouchHelper().attachToRecyclerView(rvExpandable)

        fab.setOnClickListener{
            val intent = Intent(this, AddBookActivity::class.java)
            startActivityForResult(intent, ADD_BOOK_REQUEST_CODE)

        }
    }

    private fun initViewModel(){

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        viewModel.books.observe(this, Observer { books ->

            this@MainActivity.catalog.clear()

            books!!.forEach { book ->

                if(book.isSerie){
                    var series = Series(book.serieTitle, books.filter { x -> x.serieTitle == book.serieTitle})
                    if(series !in this@MainActivity.catalog){
                        this@MainActivity.catalog.add(series)
                    }
                } else {
                    this@MainActivity.catalog.add(book)
                }

            }

            setExpandableRecyclerView()

        })

    }

    private fun setExpandableRecyclerView(){

        groupAdapter.clear()

        catalog.forEach{x ->

            if(x is Book) {

                groupAdapter.add(ExpandableGroup(ExpandableParentBook(this@MainActivity, x), false))

            } else if(x is Series){

                groupAdapter.add(

                    ExpandableGroup(ExpandableParentSerie(x), false).apply {

                        x.books.forEach { y ->

                            add(ExpandableChild(this@MainActivity, y))

                        }
                    }
                )
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//                R.id.delete_books -> {
//                viewModel.deleteAllBooks()
//                true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }


    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val inflater:LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.pop_up_windows, null)
                val popupWindow = PopupWindow(
                    view,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                view.findViewById<TextView>(R.id.tvPopUp).text = "Are you sure you want to delete?"
                val btnYesPopUp = view.findViewById<TextView>(R.id.btnYes)
                val btnNoPopUp = view.findViewById<TextView>(R.id.btnNo)

                btnNoPopUp.setOnClickListener {
                    setExpandableRecyclerView()
                    popupWindow.dismiss()
                }

                popupWindow.showAtLocation(
                    inflater.inflate(R.layout.activity_main, null),
                    Gravity.CENTER,0,0
                )

                btnYesPopUp.setOnClickListener {
                    try {
                        val bookToDelete = catalog[viewHolder.adapterPosition]
                        if(bookToDelete is Book){
                            viewModel.deleteBook(bookToDelete)
                        } else if (bookToDelete is Series){
                            bookToDelete.books.forEach { x -> viewModel.deleteBook(x) }
                        }
                        popupWindow.dismiss()
                    } catch (ex: Exception) {
                        popupWindow.dismiss()
                        setExpandableRecyclerView()
                        Toast.makeText(this@MainActivity, "Cant delete child items yet", Toast.LENGTH_LONG).show()
                    }
                }


            }
        }
        return ItemTouchHelper(callback)
    }
}


