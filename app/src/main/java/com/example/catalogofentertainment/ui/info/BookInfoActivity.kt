package com.example.catalogofentertainment.ui.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType.*
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.catalogofentertainment.R
import com.example.catalogofentertainment.model.Book
import com.example.catalogofentertainment.model.enums.BookStatus
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_add_book.*
import kotlinx.android.synthetic.main.content_book_info.*
import kotlinx.android.synthetic.main.content_book_info.ivBookCover
import kotlinx.android.synthetic.main.content_book_info.spinnerStatus

const val BOOK = "BOOK"

class BookInfoActivity : AppCompatActivity() {

    private lateinit var viewModel: BookInfoActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_book_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViewModel()
        initViews()

    }

    private fun initViews(){

        initEditText()
        initSetText()
        initLongClickListeners()

        // make sure correct image is used
        if(viewModel.book.value!!.bitmap != null) {
            ivBookCover.setImageBitmap(viewModel.book.value!!.bitmap)
        } else if(!viewModel.book.value!!.imageLinks?.smallThumbnail.isNullOrBlank()){
            Picasso.get().load(viewModel.book.value!!.imageLinks?.smallThumbnail).into(ivBookCover)
        }else   {
            ivBookCover.setImageResource(R.drawable.icon_camera)
        }

        // set spinner values and selected
        val statusSpinner = resources.getStringArray(R.array.BookStatus)
        spinnerStatus.adapter = ArrayAdapter(this, R.layout.spinner_status, statusSpinner)
        spinnerStatus.setSelection(statusSpinner.indexOfFirst { it == viewModel.book.value!!.status.toString() } )

    }

    // Initiate ViewModel
    private fun  initViewModel() {
        viewModel = ViewModelProviders.of(this).get(BookInfoActivityViewModel::class.java)
        val book = intent.getParcelableExtra<Book>(BOOK)
        supportActionBar?.title = book?.title

        viewModel.book.value = book
    }

    // function to set EditText to editable or not
    private fun setEditText(editText: EditText, inputType: Int): Boolean {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.inputType = inputType
        return true
    }

    // set all EditText on default non Editable
    private fun initEditText(){
        setEditText(tvISBN, TYPE_NULL)
        //setEditText(tvTitle, TYPE_NULL)
        setEditText(tvAuthor, TYPE_NULL)
        setEditText(tvPublisher, TYPE_NULL)
        setEditText(tvPublishDate, TYPE_NULL)
        setEditText(tvPageCount, TYPE_NULL)
        setEditText(tvLanguage, TYPE_NULL)
        //setEditText(tvDescription, TYPE_TEXT_FLAG_MULTI_LINE)
    }

    // set the value of the intent book in de TextViews
    private fun initSetText(){
        tvISBN.setText(viewModel.book.value!!.isbn)
        tvTitle.setText(viewModel.book.value!!.title)
        tvAuthor.setText(viewModel.book.value!!.authors)
        tvPublisher.setText(viewModel.book.value!!.publisher)
        tvPublishDate.setText(viewModel.book.value!!.publishedDate)
        tvPageCount.setText(viewModel.book.value!!.pageCount)
        tvLanguage.setText(viewModel.book.value!!.language)
        tvDescription.setText(viewModel.book.value!!.description)
    }

    // init al the long lcik listeners for the update
    private fun initLongClickListeners(){
        tvISBN.setOnLongClickListener { setEditText(tvISBN, TYPE_CLASS_TEXT) }
        tvTitle.setOnLongClickListener { setEditText(tvTitle, TYPE_CLASS_TEXT) }
        tvAuthor.setOnLongClickListener { setEditText(tvAuthor, TYPE_CLASS_TEXT) }
        tvPublisher.setOnLongClickListener { setEditText(tvPublisher, TYPE_CLASS_TEXT) }
        tvPublishDate.setOnLongClickListener { setEditText(tvPublishDate, TYPE_CLASS_TEXT) }
        tvPageCount.setOnLongClickListener { setEditText(tvPageCount, TYPE_CLASS_TEXT) }
        tvLanguage.setOnLongClickListener { setEditText(tvLanguage, TYPE_CLASS_TEXT) }
        tvDescription.setOnLongClickListener { setEditText(tvDescription, TYPE_TEXT_FLAG_MULTI_LINE) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_book_info, menu)
        return true
    }

    // option item to save the updated book values
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.save -> {
                viewModel.book.observe(this, Observer { book ->
                    book.isbn = tvISBN.text.toString()
                    book.title = tvTitle.text.toString()
                    book.authors= tvAuthor.text.toString()
                    book.publisher = tvPublisher.text.toString()
                    book.publishedDate = tvPublishDate.text.toString()
                    book.pageCount = tvPageCount.text.toString()
                    book.language = tvLanguage.text.toString()
                    book.description = tvDescription.text.toString()

                    when(spinnerStatus.selectedItem.toString()){
                        BookStatus.PENDING.toString() -> viewModel.book.value!!.status = BookStatus.PENDING
                        BookStatus.IN_PROGRESS.toString() -> viewModel.book.value!!.status = BookStatus.IN_PROGRESS
                        BookStatus.COMPLETED.toString() -> viewModel.book.value!!.status = BookStatus.COMPLETED
                    }

                })
                viewModel.updateBook()
                initEditText()
                Toast.makeText(this, "Changes Saved", Toast.LENGTH_LONG).show()
                return true
            } else -> return super.onOptionsItemSelected(item)
        }
    }
}
