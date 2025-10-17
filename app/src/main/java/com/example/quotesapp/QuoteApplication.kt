package com.example.quotesapp

import android.app.Application
import androidx.room.Room
import com.example.quotesapp.data.AppDatabase
import com.example.quotesapp.data.QuoteRepository

class QuoteApplication : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "quotes.db"
        ).build()
    }

    val repository: QuoteRepository by lazy {
        QuoteRepository(database.quoteDao())
    }
}
