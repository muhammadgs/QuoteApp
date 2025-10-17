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
import com.example.quotesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

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
        binding.tabRandom.setOnClickListener { viewModel.select(Tab.RANDOM) }
        binding.tabPersons.setOnClickListener { viewModel.select(Tab.PERSONS) }

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
            val drawable = ContextCompat.getDrawable(this, R.drawable.bg_tab_pill)?.mutate()
            val colorRes = if (isSelected) R.color.tab_selected else R.color.tab_unselected
            drawable?.setTint(ContextCompat.getColor(this, colorRes))
            view.background = drawable
            view.setLineSpacing(0f, 1.263f)
        }

        setPill(binding.tabMyQuotes, selected == Tab.MY)
        setPill(binding.tabRandom, selected == Tab.RANDOM)
        setPill(binding.tabPersons, selected == Tab.PERSONS)

        binding.cvTabs.setCardBackgroundColor(ContextCompat.getColor(this, R.color.tab_capsule))
        binding.root.setBackgroundColor(ContextCompat.getColor(this, R.color.background_surface))
    }
}
