package com.example.quotesapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.quotesapp.data.QuoteEntity
import com.example.quotesapp.tags.TagListActivity
import com.example.quotesapp.authors.AuthorListActivity
import com.example.quotesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as QuoteApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateTabs(Tab.MY)

        binding.tabMyQuotes.setOnClickListener { viewModel.select(Tab.MY) }
        binding.tabTags.setOnClickListener {
            startActivity(Intent(this, TagListActivity::class.java))
        }
        binding.tabAuthors.setOnClickListener {
            startActivity(Intent(this, AuthorListActivity::class.java))
        }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddQuoteActivity::class.java))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.selectedTab.collect { updateTabs(it) } }
                launch { viewModel.myQuotes.collect { renderQuoteCard(it) } }
            }
        }
    }

    private fun renderQuoteCard(quotes: List<QuoteEntity>) {
        if (quotes.isEmpty()) {
            binding.tvAuthor.text = getString(R.string.placeholder_author)
            binding.tvQuote.text = getString(R.string.placeholder_quote)
            binding.tvDate.text = getString(R.string.placeholder_date)
        } else {
            val latest = quotes.first()
            binding.tvAuthor.text = latest.attribution
            binding.tvQuote.text = latest.text
            binding.tvDate.text = latest.createdAt.toDateTime()
        }
    }

    private fun updateTabs(selected: Tab) {
        fun setPill(view: TextView, isSelected: Boolean) {
            val bgRes = if (isSelected) R.drawable.bg_pill_selected else R.drawable.bg_pill_unselected
            view.setBackgroundResource(bgRes)
            view.setLineSpacing(0f, 1.263f)
        }


        setPill(binding.tabMyQuotes, selected == Tab.MY)
        setPill(binding.tabTags, selected == Tab.TAGS)
        setPill(binding.tabAuthors, selected == Tab.AUTHORS)

        binding.cvTabs.setCardBackgroundColor(ContextCompat.getColor(this, R.color.tab_capsule))
        binding.root.setBackgroundColor(ContextCompat.getColor(this, R.color.background_surface))
    }


    private fun Long.toDisplayDate(): String {
        val df = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return df.format(Date(this))
    }

}

