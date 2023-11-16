package com.campuscoders.posterminalapp.presentation.sale.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.campuscoders.posterminalapp.databinding.FragmentProductsBinding
import com.campuscoders.posterminalapp.presentation.SaleActivity
import com.campuscoders.posterminalapp.presentation.sale.BaseViewModel
import com.campuscoders.posterminalapp.utils.FilterList
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toast
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProductsFragment: Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private var isFabMenuOpen: Boolean = false

    private lateinit var baseViewModel: BaseViewModel

    private var ftransaction: FragmentTransaction? = null

    private var saleActivity: SaleActivity? = null

    private val productsAdapter by lazy {
        ProductsAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProductsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var job: Job? = null

        binding.textInputLayoutSearch.hide()

        binding.textInputEditTextSearch.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                it?.let {
                    if(it.toString().isNotEmpty()) {
                        productsAdapter.products = FilterList.bySearch(baseViewModel.statusProductsList.value?.data?: listOf(),it.toString())
                    } else {
                        productsAdapter.products = baseViewModel.statusProductsList.value?.data?: listOf()
                    }
                    productsAdapter.notifyDataSetChanged()
                }
            }
        }

        saleActivity = activity as? SaleActivity
        saleActivity?.setEnabledShoppingCartIcon(true)

        setFabMenu()

        baseViewModel = ViewModelProvider(requireActivity())[BaseViewModel::class.java]

        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)
        binding.recyclerViewProducts.adapter = productsAdapter
        binding.recyclerViewProducts.layoutManager = staggeredGridLayoutManager

        val args = arguments
        args?.let {
            val categoryId = it.getString("category_id")
            val topBarTitle = "Kategoriler/${changeTopBarTitle(categoryId.toString())}"
            saleActivity?.changeSaleActivityTopBarTitle(topBarTitle)
            baseViewModel.getProductsByCategoryId(categoryId!!)
        }

        productsAdapter.setOnItemClickListener {
            baseViewModel.addProduct(it.toString())
        }

        observer()
    }

    private fun observer() {
        baseViewModel.statusProductsList.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarProducts.hide()
                    binding.linearLayoutNoProduct.hide()
                    if (it.data!!.isNotEmpty()) {
                        productsAdapter.products = it.data
                    } else {
                        toast(requireContext(),"No data",false)
                        binding.linearLayoutNoProduct.show()
                    }
                }
                is Resource.Loading -> {
                    binding.progressBarProducts.show()
                }
                is Resource.Error -> {
                    binding.progressBarProducts.hide()
                    toast(requireContext(),it.message?:"Error Products",false)
                }
            }
        }
    }

    private fun changeTopBarTitle(categoryId: String): String {
        return when(categoryId) {
            "1" -> "Yiyecek"
            "2" -> "İçecek"
            "3" -> "Meyve"
            "4" -> "Kişisel Bakım"
            "5" -> "Dondurma"
            "6" -> "Bebek"
            "7" -> "Fırın"
            "8" -> "Sebze"
            "9" -> "Atıştırmalık"
            "10" -> "İlaç"
            "11" -> "Ev Eşya"
            else -> "null"
        }
    }

    private fun setFabMenu() {
        binding.extendedFabSettings.hide()
        binding.floatingActionButtonSearch.hide()
        binding.floatingActionButtonBarcode.hide()

        binding.floatingActionButtonMainAdd.setOnClickListener {
            toggleFabMenu()
        }
        binding.extendedFabSettings.setOnClickListener {
            toggleFabMenu()
        }
        binding.floatingActionButtonBack.setOnClickListener {

        }
        binding.floatingActionButtonSearch.setOnClickListener {
            if (binding.textInputLayoutSearch.isVisible) {
                productsAdapter.products = baseViewModel.statusProductsList.value?.data?: listOf()
                binding.textInputLayoutSearch.hide()
            } else {
                binding.textInputLayoutSearch.show()
            }
            toggleFabMenu()
        }
        binding.floatingActionButtonBarcode.setOnClickListener {
            saleActivity?.goToBarcodeActivity()
        }
    }

    private fun toggleFabMenu() {
        if (isFabMenuOpen) {
            binding.extendedFabSettings.hide()
            binding.floatingActionButtonSearch.hide()
            binding.floatingActionButtonBarcode.hide()
            binding.floatingActionButtonMainAdd.show()
        } else {
            binding.floatingActionButtonMainAdd.hide()
            binding.extendedFabSettings.show()
            binding.floatingActionButtonSearch.show()
            binding.floatingActionButtonBarcode.show()
        }
        isFabMenuOpen = !isFabMenuOpen
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}