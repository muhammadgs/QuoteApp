package com.example.quotesapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.quotesapp.MainViewModel
import com.example.quotesapp.MainViewModelFactory
import com.example.quotesapp.QuoteApplication
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentMyQuotesBinding
import com.example.quotesapp.toDateTime
import kotlinx.coroutines.launch

class MyQuotesFragment : Fragment() {

    private var _binding: FragmentMyQuotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory((requireActivity().application as QuoteApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyQuotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myQuotes.collect { quotes ->
                    if (quotes.isEmpty()) {
                        binding.tvAuthor.setText(R.string.placeholder_author)
                        binding.tvQuote.setText(R.string.placeholder_quote)
                        binding.tvDate.setText(R.string.placeholder_date)
                    } else {
                        val latest = quotes.first()
                        binding.tvAuthor.text = latest.attribution
                        binding.tvQuote.text = latest.text
                        binding.tvDate.text = latest.createdAt.toDateTime()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
