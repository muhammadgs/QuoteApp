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
import com.example.quotesapp.databinding.ActivityMainBinding
import com.example.quotesapp.home.AuthorsFragment
import com.example.quotesapp.home.MyQuotesFragment
import com.example.quotesapp.home.TagsFragment
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as QuoteApplication).repository)
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewModel.select(Tab.entries[position])
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MainPagerAdapter(this)
        binding.viewPager.offscreenPageLimit = Tab.entries.size

        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)

        binding.tabMyQuotes.setOnClickListener { viewModel.select(Tab.MY) }
        binding.tabTags.setOnClickListener { viewModel.select(Tab.TAGS) }
        binding.tabAuthors.setOnClickListener { viewModel.select(Tab.AUTHORS) }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddQuoteActivity::class.java))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.selectedTab.collect { tab ->
                        if (binding.viewPager.currentItem != tab.ordinal) {
                            binding.viewPager.setCurrentItem(tab.ordinal, true)
                        }
                        updateTabs(tab)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
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

    private inner class MainPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = Tab.entries.size

        override fun createFragment(position: Int): Fragment = when (Tab.entries[position]) {
            Tab.MY -> MyQuotesFragment()
            Tab.TAGS -> TagsFragment()
            Tab.AUTHORS -> AuthorsFragment()
        }
    }
}

