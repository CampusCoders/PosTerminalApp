package com.campuscoders.posterminalapp.presentation.edit.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentEditProductBinding
import com.campuscoders.posterminalapp.presentation.UpdateOrAddActivity
import com.campuscoders.posterminalapp.presentation.sale.views.BarcodeScannerActivity
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditProductFragment : Fragment() {

    private var _binding: FragmentEditProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EditProductViewModel

    private var ftransaction: FragmentTransaction? = null

    private var isFabMenuOpen: Boolean = false

    private var productId = 0

    private val editProductAdapter by lazy {
        EditProductAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[EditProductViewModel::class.java]
        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        arguments?.let {
            val categoryId = it.getString("categoryId")
            viewModel.getProductsByCategoryId(categoryId.toString())
        }

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        binding.recyclerViewEditProduct.adapter = editProductAdapter
        binding.recyclerViewEditProduct.layoutManager = staggeredGridLayoutManager

        editProductAdapter.setOnItemClickListener {
            // show product detail popup
        }

        editProductAdapter.setOnLongItemClickListener {
            showEditOrDeletePopup(it)
            productId = it
        }

        binding.extendedFabSettings.hide()
        binding.floatingActionButtonSearch.hide()
        binding.floatingActionButtonBarcode.hide()
        binding.floatingActionButtonAdd.hide()

        binding.floatingActionButtonBarcode.setOnClickListener {
            val intent = Intent(requireActivity(), BarcodeScannerActivity::class.java)
            startActivity(intent)
        }
        binding.floatingActionButtonAdd.setOnClickListener {
            val intent = Intent(requireActivity(), UpdateOrAddActivity::class.java)
            intent.putExtra("from", "product")
            intent.putExtra("category_or_product_id", -1)
            startActivity(intent)
        }

        binding.floatingActionButtonMainAdd.setOnClickListener {
            toggleFabMenu()
        }

        binding.floatingActionButtonBack.setOnClickListener {

        }

        binding.extendedFabSettings.setOnClickListener {
            toggleFabMenu()
        }

        observer()
    }

    private fun observer() {
        viewModel.statusProductsList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBarEditProduct.hide()
                    editProductAdapter.productsList = it.data!!
                    if (it.data.isNotEmpty()) {
                        binding.linearLayoutNoProduct.hide()
                    } else {
                        binding.linearLayoutNoProduct.show()
                    }
                }

                is Resource.Loading -> {
                    binding.progressBarEditProduct.show()
                }

                is Resource.Error -> {
                    binding.progressBarEditProduct.hide()
                    toast(requireContext(), it.message ?: "Error!", false)
                }
            }
        }
        viewModel.statusDeleteProducts.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBarEditProduct.hide()
                    editProductAdapter.updateProductsList(productId)
                    toast(requireContext(), "Ürün silindi", false)
                }

                is Resource.Loading -> {
                    // loading popup
                    binding.progressBarEditProduct.show()
                }

                is Resource.Error -> {
                    binding.progressBarEditProduct.hide()
                    toast(requireContext(), it.message ?: "Error!", false)
                }
            }
        }
    }

    private fun showEditOrDeletePopup(productId: Int) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.pop_up_edit_or_delete_item, null)
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(dialogView).create()

        val linearEdit = dialogView.findViewById<LinearLayout>(R.id.linearLayoutEdit)
        val linearDelete = dialogView.findViewById<LinearLayout>(R.id.linearLayoutDelete)

        linearEdit.setOnClickListener {
            val intent = Intent(requireActivity(), UpdateOrAddActivity::class.java)
            intent.putExtra("from", "product")
            intent.putExtra("category_or_product_id", productId)
            startActivity(intent)
        }

        linearDelete.setOnClickListener {
            viewModel.deleteProduct(productId)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun toggleFabMenu() {
        if (isFabMenuOpen) {
            binding.extendedFabSettings.hide()
            binding.floatingActionButtonSearch.hide()
            binding.floatingActionButtonBarcode.hide()
            binding.floatingActionButtonAdd.hide()
            binding.floatingActionButtonMainAdd.show()
        } else {
            binding.extendedFabSettings.show()
            binding.floatingActionButtonSearch.show()
            binding.floatingActionButtonBarcode.show()
            binding.floatingActionButtonAdd.show()
            binding.floatingActionButtonMainAdd.hide()
        }
        isFabMenuOpen = !isFabMenuOpen
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}