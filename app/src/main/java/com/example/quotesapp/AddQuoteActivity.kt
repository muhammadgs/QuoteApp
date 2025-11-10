package com.example.quotesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.quotesapp.databinding.ActivityAddQuoteBinding
import kotlinx.coroutines.launch

class AddQuoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddQuoteBinding

    private val viewModel: AddQuoteViewModel by viewModels {
        AddQuoteViewModelFactory((application as QuoteApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureInputValidation()

        binding.btnDiscard.setOnClickListener { finish() }
        binding.btnSave.setOnClickListener { attemptSave() }
    }

    private fun configureInputValidation() {
        binding.etQuote.doAfterTextChanged { binding.tilQuote.error = null }
        binding.etAttribution.doAfterTextChanged { binding.tilAttribution.error = null }
    }


    private fun attemptSave() {
        // ✅ EKSİK İKİ DEĞİŞKEN
        val quoteText = binding.etQuote.text?.toString()?.trim().orEmpty()
        val attribution = binding.etAttribution.text?.toString()?.trim().orEmpty()

        val tags = binding.etTags.text?.toString()?.trim().orEmpty()
        val notes = binding.etNotes.text?.toString()?.trim().orEmpty()

        var hasError = false
        if (quoteText.isEmpty()) {
            binding.tilQuote.error = getString(R.string.error_required)
            hasError = true
        }
        if (attribution.isEmpty()) {
            binding.tilAttribution.error = getString(R.string.error_required)
            hasError = true
        }
        if (hasError) return

        lifecycleScope.launch {
            try {
                viewModel.saveQuote(
                    text = quoteText,
                    attribution = attribution,
                    tags = tags.takeIf { it.isNotEmpty() },
                    notes = notes.takeIf { it.isNotEmpty() },
                    now = System.currentTimeMillis()
                )
                finish()
            } catch (t: Throwable) {
                Toast.makeText(
                    this@AddQuoteActivity,
                    R.string.error_saving_quote,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
