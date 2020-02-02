package pl.kamilszustak.shark

import BookRepositoryImpl
import FlightRepositoryImpl
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private val bookRepository = BookRepositoryImpl(application)
    private val flightRepository = FlightRepositoryImpl(application)

    init {
        println(bookRepository.bookTitle.value)
        bookRepository.bookTitle.value = "New Book"
        println(bookRepository.bookTitle.value)

        println(flightRepository.flightCode.value)
        flightRepository.flightCode.value = "QWE456"
        println(flightRepository.flightCode.value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}