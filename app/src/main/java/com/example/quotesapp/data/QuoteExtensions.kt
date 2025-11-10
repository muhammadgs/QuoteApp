package com.example.quotesapp.data

fun QuoteEntity.tagList(): List<String> = tags
    ?.split(",")
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: emptyList()

val QuoteEntity.authorName: String
    get() = attribution
