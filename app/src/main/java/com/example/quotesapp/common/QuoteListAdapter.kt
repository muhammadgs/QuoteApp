package com.example.quotesapp.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.data.QuoteEntity
import com.example.quotesapp.data.authorName
import com.example.quotesapp.data.tagList
import com.example.quotesapp.databinding.ItemQuoteBinding
import com.example.quotesapp.toDateTime

class QuoteListAdapter : ListAdapter<QuoteEntity, QuoteListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: QuoteEntity) {
            binding.tvAuthor.text = item.authorName
            binding.tvQuote.text = item.text
            binding.tvDate.text = item.createdAt.toDateTime()

            val tags = item.tagList()
            binding.tvTags.isVisible = tags.isNotEmpty()
            binding.tvTags.text = tags.joinToString(separator = "  ") { "#${it}" }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<QuoteEntity>() {
        override fun areItemsTheSame(oldItem: QuoteEntity, newItem: QuoteEntity): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: QuoteEntity, newItem: QuoteEntity): Boolean = oldItem == newItem
    }
}
