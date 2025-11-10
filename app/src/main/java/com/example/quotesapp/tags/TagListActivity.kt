package com.example.quotesapp.tags

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
import com.example.quotesapp.data.tagList
import com.example.quotesapp.databinding.ActivityListBinding
import com.example.quotesapp.quotes.FilteredQuotesActivity.Companion.createForTag
import kotlinx.coroutines.launch
import java.util.Locale

class TagListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private val repository by lazy { (application as QuoteApplication).repository }

    private val adapter = SimpleItemAdapter { tag ->
        startActivity(createForTag(this, tag))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.title_tags)
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.tvListTitle.text = getString(R.string.title_tags)
        binding.tvEmpty.setText(R.string.empty_state_tags)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.myQuotes().collect { quotes ->
                    val tags = quotes
                        .flatMap { it.tagList() }
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .distinctBy { it.lowercase(Locale.getDefault()) }
                        .sortedWith(String.CASE_INSENSITIVE_ORDER)

                    binding.recyclerView.isVisible = tags.isNotEmpty()
                    binding.tvEmpty.isVisible = tags.isEmpty()
                    adapter.submitList(tags)
                }
            }
        }
    }
}
