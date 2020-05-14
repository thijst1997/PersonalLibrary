package com.example.catalogofentertainment.ui.addBook

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.catalogofentertainment.R
import com.example.catalogofentertainment.api.BooksApiRepository
import com.example.catalogofentertainment.model.Book
import com.example.catalogofentertainment.model.enums.BookStatus
import com.example.catalogofentertainment.ui.main.ADD_BOOK_REQUEST_CODE
import com.example.catalogofentertainment.ui.main.MainActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_add_book.*
import kotlinx.android.synthetic.main.content_add_book.ivBookCover
import kotlinx.android.synthetic.main.expandable_item_parent.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import java.net.URL

const val REQUEST_IMAGE_CAPTURE = 1
const val MAIN_ACTIVITY_REQUEST_CODE = 100

class AddBookActivity : AppCompatActivity() {

    private lateinit var viewModel: AddBookActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_add_book)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.addBook)

        initView()
        initViewModel()
        initAutoCompSpinner()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            if(viewModel.book.value!!.bitmap != null) {
                ivBookCover.setImageBitmap(viewModel.book.value!!.bitmap)
            } else if(!viewModel.book.value!!.imageLinks?.smallThumbnail.isNullOrBlank()){
                Picasso.get().load(viewModel.book.value!!.imageLinks?.smallThumbnail).into(ivBookCover)
            }else {
                ivBookCover.setImageResource(R.drawable.icon_camera)
            }

        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            if(viewModel.book.value!!.bitmap != null) {
                ivBookCover.setImageBitmap(viewModel.book.value!!.bitmap)
            } else if(!viewModel.book.value!!.imageLinks?.smallThumbnail.isNullOrBlank()){
                Picasso.get().load(viewModel.book.value!!.imageLinks?.smallThumbnail).into(ivBookCover)
            }else {
                ivBookCover.setImageResource(R.drawable.icon_camera)
            }
        }
    }

    // Initiate ViewModel
    private fun  initViewModel() { viewModel = ViewModelProviders.of(this).get(AddBookActivityViewModel::class.java) }

    // initiate View elements
    private fun initView() {

        // initiate spinner for status
        val statusSpinner = resources.getStringArray(R.array.BookStatus)
        spinnerStatus.adapter = ArrayAdapter(this, R.layout.spinner_status, statusSpinner)
        spinnerStatus.setSelection(0)

        // clickListeners for bookImageView and submit button
        ivBookCover.setOnClickListener { onBookCoverClick() }
        btnSubmit.setOnClickListener { onAddBookClick() }

    }

    private fun initAutoCompSpinner(){

        // initiate spinner for serie autoComplete
        viewModel.serieTitles.observe(this, Observer { books ->
            var serieTitles = books.filter { x -> x.serieTitle != null }.map { it.serieTitle }.distinct()
            etSeries.setAdapter(ArrayAdapter<String>(this, R.layout.spinner_autocomplete, serieTitles))
        })

        etSeries.threshold = 1

    }

    // clickListener for taking picture of the book
    private fun onBookCoverClick() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    // clickListener for book submission
    private fun onAddBookClick() {

        if(!viewModel.isApi.value!!) {
            viewModel.book.value!!.isbn = etISBN.text.toString()
            viewModel.book.value!!.title = etTitle.text.toString()
            viewModel.book.value!!.authors = etAuthor.text.toString()
            viewModel.book.value!!.publisher = etPublisher.text.toString()
            viewModel.book.value!!.publishedDate = etPublishDate.text.toString()
            viewModel.book.value!!.language = etLanguage.text.toString()
            viewModel.book.value!!.pageCount = etPageNr.text.toString()
            viewModel.book.value!!.description = etDescription.text.toString()
        }

        if(!etSeries.text.toString().isNullOrBlank()){
            viewModel.book.value!!.serieTitle = etSeries.text.toString()
            viewModel.book.value!!.isSerie = true
        }

        when(spinnerStatus.selectedItem.toString()){
            BookStatus.PENDING.toString() -> viewModel.book.value!!.status = BookStatus.PENDING
            BookStatus.IN_PROGRESS.toString() -> viewModel.book.value!!.status = BookStatus.IN_PROGRESS
            BookStatus.COMPLETED.toString() -> viewModel.book.value!!.status = BookStatus.COMPLETED
        }


        viewModel.addBook()

        val intent = Intent(this, MainActivity::class.java)
        startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CODE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_book, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.barcode_scanner -> {
                // start up the bar code scanner
                val integrator = IntentIntegrator(this)
                integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13)
                integrator.setOrientationLocked(false)
                integrator.initiateScan()

                return true
            } else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Bar code scan result
        val barcodeResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (barcodeResult != null) {



            // Api request with isbn result from the bar code scan
            viewModel.requestBook(barcodeResult.contents)

            // Fill placeholders with response value to prep for db save
            viewModel.book.observe(this, Observer { book ->
                etISBN.setText(book.isbn)
                etTitle.setText(book.title)
                etAuthor.setText(book.authors)
                etLanguage.setText(book.language)
                etPageNr.setText(book.pageCount)
                etPublishDate.setText(book.publishedDate)
                etPublisher.setText(book.publisher)
                etDescription.setText(book.description)

                if(book.bitmap != null) {
                    ivBookCover.setImageBitmap(book.bitmap)
                } else if(!book.imageLinks?.smallThumbnail.isNullOrBlank()){
                    Picasso.get().load(book.imageLinks?.smallThumbnail).into(ivBookCover) // doesn't work well
                }else {
                    ivBookCover.setImageResource(R.drawable.icon_camera)
                }

            })

            // Toast when viewModel returns an error
            viewModel.error.observe(this, Observer {
                if(!it.isNullOrBlank()){
                    Toast.makeText(this,it,Toast.LENGTH_LONG).show()
                }
            })

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // set camera photo as image
            val imageBitmap = data?.extras?.get("data") as Bitmap
            ivBookCover.setImageBitmap(imageBitmap)
            viewModel.book.value!!.bitmap = imageBitmap

        } else {
            return super.onActivityResult(requestCode, resultCode, data)
        }

    }

}
