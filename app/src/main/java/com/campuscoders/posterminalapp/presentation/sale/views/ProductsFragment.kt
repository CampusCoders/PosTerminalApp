package com.campuscoders.posterminalapp.presentation.sale.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.campuscoders.posterminalapp.databinding.FragmentProductsBinding
import com.campuscoders.posterminalapp.presentation.SaleActivity
import com.campuscoders.posterminalapp.presentation.sale.BaseViewModel
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toast

class ProductsFragment: Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

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

        baseViewModel = ViewModelProvider(requireActivity())[BaseViewModel::class.java]

        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)
        binding.recyclerViewProducts.adapter = productsAdapter
        binding.recyclerViewProducts.layoutManager = staggeredGridLayoutManager

        saleActivity = activity as? SaleActivity
        saleActivity?.setEnabledShoppingCartIcon(true)

        val args = arguments
        args?.let {
            val categoryId = it.getString("category_id")
            val topBarTitle = "Kategoriler/${changeTopBarTitle(categoryId.toString())}"
            saleActivity?.changeSaleActivityTopBarTitle(topBarTitle)
            baseViewModel.getProductsByCategoryId(categoryId!!)
        }

        /*
        productsAdapter.setOnItemClickListener {
            // TestActivity'deki shopping textview'i arttır
            hashmapOfProducts = saleActivity?.getHashmap()!!
            val productCount = hashmapOfProducts[it.toString()]
            if (productCount == null) {
                hashmapOfProducts[it.toString()] = 1
            } else {
                hashmapOfProducts[it.toString()] = productCount + 1
            }
            saleActivity?.setShoppingCart(hashmapOfProducts)
        }
         */

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
        baseViewModel.statusProductsList.observe(viewLifecycleOwner) {
            if (it is Resource.Error) {
                toast(requireContext(),it.message?:"statusProductList hata", false)
            }
        }
        baseViewModel.statusShoppingCartQuantity.observe(viewLifecycleOwner) {
            saleActivity?.setShoppingCart(it.toString())
        }
        baseViewModel.statusTotal.observe(viewLifecycleOwner) {
            saleActivity?.setShoppingCartTotal(it)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}