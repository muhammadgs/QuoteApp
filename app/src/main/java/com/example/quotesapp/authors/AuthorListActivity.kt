package com.example.quotesapp.authors

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotesapp.QuoteApplication
import com.example.quotesapp.R
import com.example.quotesapp.common.SimpleItemAdapter
import com.example.quotesapp.data.authorName
import com.example.quotesapp.databinding.ActivityListBinding
import com.example.quotesapp.quotes.FilteredQuotesActivity.Companion.createForAuthor
import kotlinx.coroutines.launch
import java.util.Locale

class AuthorListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private val repository by lazy { (application as QuoteApplication).repository }

    private val adapter = SimpleItemAdapter { author ->
        startActivity(createForAuthor(this, author))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.title_authors)
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.tvListTitle.text = getString(R.string.title_authors)
        binding.tvEmpty.setText(R.string.empty_state_authors)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.myQuotes().collect { quotes ->
                    val authors = quotes
                        .map { it.authorName.trim() }
                        .filter { it.isNotEmpty() }
                        .distinctBy { it.lowercase(Locale.getDefault()) }
                        .sortedWith(String.CASE_INSENSITIVE_ORDER)

                    binding.recyclerView.isVisible = authors.isNotEmpty()
                    binding.tvEmpty.isVisible = authors.isEmpty()
                    adapter.submitList(authors)
                }
            }
        }
    }
}
