package com.example.quotesapp.quotes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotesapp.QuoteApplication
import com.example.quotesapp.R
import com.example.quotesapp.common.QuoteListAdapter
import com.example.quotesapp.data.authorName
import com.example.quotesapp.data.tagList
import com.example.quotesapp.databinding.ActivityListBinding
import kotlinx.coroutines.launch
import java.util.Locale

class FilteredQuotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private val repository by lazy { (application as QuoteApplication).repository }

    private val adapter = QuoteListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra(EXTRA_FILTER_TYPE)?.let { FilterType.valueOf(it) }
        val value = intent.getStringExtra(EXTRA_FILTER_VALUE)

        if (type == null || value.isNullOrBlank()) {
            finish()
            return
        }

        binding.toolbar.title = when (type) {
            FilterType.TAG -> getString(R.string.title_tags)
            FilterType.AUTHOR -> getString(R.string.title_authors)
        }
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.tvListTitle.text = when (type) {
            FilterType.TAG -> getString(R.string.title_quotes_for_tag, value)
            FilterType.AUTHOR -> getString(R.string.title_quotes_for_author, value)
        }
        binding.tvEmpty.setText(R.string.empty_state_quotes)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.myQuotes().collect { quotes ->
                    val filtered = when (type) {
                        FilterType.TAG -> {
                            val target = value.lowercase(Locale.getDefault())
                            quotes.filter { entity ->
                                entity.tagList().any { it.lowercase(Locale.getDefault()) == target }
                            }
                        }
                        FilterType.AUTHOR -> quotes.filter { entity ->
                            entity.authorName.equals(value, ignoreCase = true)
                        }
                    }

                    binding.recyclerView.isVisible = filtered.isNotEmpty()
                    binding.tvEmpty.isVisible = filtered.isEmpty()
                    adapter.submitList(filtered)
                }
            }
        }
    }

    companion object {
        private const val EXTRA_FILTER_TYPE = "filter_type"
        private const val EXTRA_FILTER_VALUE = "filter_value"

        fun createForTag(context: Context, tag: String): Intent =
            Intent(context, FilteredQuotesActivity::class.java).apply {
                putExtra(EXTRA_FILTER_TYPE, FilterType.TAG.name)
                putExtra(EXTRA_FILTER_VALUE, tag)
            }

        fun createForAuthor(context: Context, author: String): Intent =
            Intent(context, FilteredQuotesActivity::class.java).apply {
                putExtra(EXTRA_FILTER_TYPE, FilterType.AUTHOR.name)
                putExtra(EXTRA_FILTER_VALUE, author)
            }
    }

    private enum class FilterType { TAG, AUTHOR }
}
