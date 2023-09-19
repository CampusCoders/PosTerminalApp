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
import com.campuscoders.posterminalapp.databinding.FragmentEditCategoryBinding
import com.campuscoders.posterminalapp.presentation.UpdateOrAddActivity
import com.campuscoders.posterminalapp.presentation.sale.views.BarcodeScannerActivity
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditCategoryFragment: Fragment() {

    private var _binding: FragmentEditCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EditCategoryViewModel

    private var ftransaction: FragmentTransaction? = null

    private var isFabMenuOpen: Boolean = false

    private var categoryId = 0

    private val editCategoryAdapter by lazy {
        EditCategoryAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[EditCategoryViewModel::class.java]
        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)
        binding.recyclerViewEditCategory.adapter = editCategoryAdapter
        binding.recyclerViewEditCategory.layoutManager = staggeredGridLayoutManager

        editCategoryAdapter.setOnItemClickListener {
            val editProductFragment = EditProductFragment()
            val bundle = Bundle()
            bundle.putString("categoryId", it.toString())
            editProductFragment.arguments = bundle
            // move to product fragment (callback - viewpager logic)
        }

        editCategoryAdapter.setOnLongItemCliclListener {
            showEditOrDeletePopup(it)
            categoryId = it
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
            val intent = Intent(requireActivity(),UpdateOrAddActivity::class.java)
            intent.putExtra("from","category")
            intent.putExtra("category_or_product_id",-1)
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
        viewModel.statusCategoriesList.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarCategories.hide()
                    editCategoryAdapter.categoriesList = it.data!!
                    if (it.data.isNotEmpty()) {
                        binding.linearLayoutNoCategory.hide()
                    } else {
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
        viewModel.statusDeleteCategory.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarCategories.hide()
                    editCategoryAdapter.updateCategoriesList(categoryId)
                    toast(requireContext(),"Kategori silindi",false)
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

    private fun showEditOrDeletePopup(categoryId: Int) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.pop_up_edit_or_delete_item,null)
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(dialogView).create()

        val linearEdit = dialogView.findViewById<LinearLayout>(R.id.linearLayoutEdit)
        val linearDelete = dialogView.findViewById<LinearLayout>(R.id.linearLayoutDelete)

        linearEdit.setOnClickListener {
            val intent = Intent(requireActivity(),UpdateOrAddActivity::class.java)
            intent.putExtra("from","category")
            intent.putExtra("category_or_product_id",categoryId)

            startActivity(intent)
        }

        linearDelete.setOnClickListener {
            viewModel.deleteCategory(categoryId)
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