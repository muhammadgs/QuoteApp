package com.example.quotesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quotesapp.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quotes = listOf(
        "“Be yourself; everyone else is already taken.” – Oscar Wilde",
        "“The only way to do great work is to love what you do.” – Steve Jobs",
        "“In the middle of difficulty lies opportunity.” – Albert Einstein",
        "“Simplicity is the ultimate sophistication.” – Leonardo da Vinci"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İlk alıntı

        binding.tvQuote.text = quotes.random()


        // Buton tıklaması
        binding.fabAdd.setOnClickListener {
            val randomIndex = Random.nextInt(quotes.size)
            binding.tvQuote.text = quotes[randomIndex]
        }
    }
}
