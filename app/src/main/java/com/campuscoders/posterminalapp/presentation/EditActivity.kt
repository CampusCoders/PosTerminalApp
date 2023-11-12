package com.campuscoders.posterminalapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.campuscoders.posterminalapp.databinding.ActivityEditBinding
import com.campuscoders.posterminalapp.presentation.edit.views.EditProductFragment
import com.campuscoders.posterminalapp.presentation.edit.views.PageAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    private var pageAdapter: PageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pageAdapter = PageAdapter(supportFragmentManager,lifecycle)
        binding.viewPager2.adapter = pageAdapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager2) {tab,position ->
            when(position) {
                0 -> {tab.text = "SERVİSLER"}
                1 -> {tab.text = "KATEGORİLER"}
            }
        }.attach()
    }

    fun changeFragment(categoryId: String) {
        val editProductFragment = EditProductFragment()
        val bundle = Bundle()
        bundle.putString("categoryId", categoryId)
        editProductFragment.arguments = bundle
        pageAdapter?.changeState(editProductFragment)
        binding.viewPager2.adapter = pageAdapter
        binding.viewPager2.currentItem = 1
    }

}