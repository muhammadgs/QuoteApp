package com.example.quotesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quotesapp.data.QuoteRepository

class AddQuoteViewModel(private val repository: QuoteRepository) : ViewModel() {
    suspend fun saveQuote(
        text: String,
        author: String,
        tags: String?,
        notes: String?,
        now: Long
    ) {
        repository.add(text, author, tags, notes, now)
    }
}

class AddQuoteViewModelFactory(private val repository: QuoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddQuoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddQuoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
