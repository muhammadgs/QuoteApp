package com.example.quotesapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotesapp.QuoteApplication
import com.example.quotesapp.R
import com.example.quotesapp.common.SimpleItemAdapter
import com.example.quotesapp.data.authorName
import com.example.quotesapp.databinding.FragmentListBinding
import com.example.quotesapp.quotes.FilteredQuotesActivity
import kotlinx.coroutines.launch
import java.util.Locale

class AuthorsFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val repository by lazy { (requireActivity().application as QuoteApplication).repository }

    private val adapter = SimpleItemAdapter { author ->
        val intent = FilteredQuotesActivity.createForAuthor(requireContext(), author)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvListTitle.setText(R.string.title_authors)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.myQuotes().collect { quotes ->
                    val authors = quotes
                        .map { it.authorName.trim() }
                        .filter { it.isNotEmpty() }
                        .distinctBy { it.lowercase(Locale.getDefault()) }
                        .sortedWith(String.CASE_INSENSITIVE_ORDER)

                    binding.recyclerView.isVisible = authors.isNotEmpty()
                    binding.tvEmpty.isVisible = authors.isEmpty()
                    binding.tvEmpty.setText(R.string.empty_state_authors)
                    adapter.submitList(authors)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
