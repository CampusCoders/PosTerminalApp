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
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentCategoriesBinding
import com.campuscoders.posterminalapp.presentation.SaleActivity
import com.campuscoders.posterminalapp.presentation.sale.CategoriesViewModel
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toast

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private var isFabMenuOpen: Boolean = false

    private var saleActivity: SaleActivity? = null

    private lateinit var viewModel: CategoriesViewModel

    private var ftransaction: FragmentTransaction? = null

    private val categoriesAdapter by lazy {
        CategoriesAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCategoriesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saleActivity = activity as? SaleActivity

        setFabMenu()

        viewModel = ViewModelProvider(requireActivity())[CategoriesViewModel::class.java]

        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        binding.recyclerViewCategories.adapter = categoriesAdapter
        binding.recyclerViewCategories.layoutManager = staggeredGridLayoutManager

        val saleActivity = (activity as SaleActivity)
        saleActivity.setEnabledShoppingCartIcon(true)
        saleActivity.changeSaleActivityTopBarTitle("Kategoriler")

        categoriesAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putString("category_id",it.toString())
            val productsFragment = ProductsFragment()
            productsFragment.arguments = bundle
            ftransaction?.let { ft ->
                ft.setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                ft.replace(R.id.fragmentContainerViewSaleActivity,productsFragment)
                ft.addToBackStack(null)
                ft.commit()
            }
        }
        observer()
    }

    private fun observer() {
        viewModel.statusCategoriesList.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarCategories.hide()
                    binding.linearLayoutNoCategory.hide()
                    if (it.data!!.isNotEmpty()) {
                        categoriesAdapter.categoriesList = it.data
                    } else {
                        toast(requireContext(),"No data",false)
                        binding.linearLayoutNoCategory.show()
                    }
                }
                is Resource.Loading -> {
                    binding.progressBarCategories.show()
                }
                is Resource.Error -> {
                    binding.progressBarCategories.hide()
                    toast(requireContext(),it.message?:"Error Categories",false)
                }
            }
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