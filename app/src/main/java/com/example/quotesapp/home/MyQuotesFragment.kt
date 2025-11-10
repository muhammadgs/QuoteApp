package com.example.quotesapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotesapp.MainViewModel
import com.example.quotesapp.MainViewModelFactory
import com.example.quotesapp.QuoteApplication
import com.example.quotesapp.common.QuoteListAdapter
import com.example.quotesapp.databinding.FragmentMyQuotesBinding
import kotlinx.coroutines.launch

class MyQuotesFragment : Fragment() {

    private var _binding: FragmentMyQuotesBinding? = null
    private val binding get() = _binding!!

    private val adapter = QuoteListAdapter()

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

        binding.rvQuotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuotes.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myQuotes.collect { quotes ->
                    adapter.submitList(quotes)
                    binding.rvQuotes.isVisible = quotes.isNotEmpty()
                    binding.tvEmpty.isVisible = quotes.isEmpty()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
