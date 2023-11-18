package com.campuscoders.posterminalapp.presentation.edit.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentEditCategoryBinding
import com.campuscoders.posterminalapp.presentation.EditActivity
import com.campuscoders.posterminalapp.presentation.UpdateOrAddActivity
import com.campuscoders.posterminalapp.presentation.edit.EditCategoryViewModel
import com.campuscoders.posterminalapp.utils.FilterList
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditCategoryFragment: Fragment() {

    private var _binding: FragmentEditCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EditCategoryViewModel

    private var ftransaction: FragmentTransaction? = null

    private var editActivity: EditActivity? = null

    private var isFabMenuOpen: Boolean = false

    private var categoryId = 0

    private val editCategoryAdapter by lazy {
        EditCategoryAdapter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        editActivity = context as EditActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[EditCategoryViewModel::class.java]

        var job: Job? = null

        binding.textInputLayoutSearch.hide()

        binding.textInputEditTextSearch.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        editCategoryAdapter.categoriesList = FilterList.bySearch(viewModel.statusCategoriesList.value?.data?: listOf(),it.toString())
                        editCategoryAdapter.notifyDataSetChanged()
                    } else {
                        editCategoryAdapter.categoriesList = viewModel.statusCategoriesList.value?.data?: listOf()
                    }
                }
            }
        }

        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)
        binding.recyclerViewEditCategory.adapter = editCategoryAdapter
        binding.recyclerViewEditCategory.layoutManager = staggeredGridLayoutManager

        editCategoryAdapter.setOnItemClickListener {
            binding.textInputEditTextSearch.setText("")
            editActivity?.changeFragment(it.toString())
        }

        editCategoryAdapter.setOnLongItemCliclListener {
            showEditOrDeletePopup(it)
            categoryId = it
        }

        setFabMenu()

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
            intent.putExtra(requireActivity().getString(R.string.navigation_from),requireActivity().getString(R.string.navigation_from_category))
            intent.putExtra(requireActivity().getString(R.string.category_id_or_product_id), categoryId.toString())
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
            dialog.dismiss()
        }

        linearDelete.setOnClickListener {
            viewModel.deleteCategory(categoryId)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setFabMenu() {
        binding.extendedFabSettings.hide()
        binding.floatingActionButtonSearch.hide()
        binding.floatingActionButtonAdd.hide()

        binding.floatingActionButtonAdd.setOnClickListener {
            val intent = Intent(requireActivity(),UpdateOrAddActivity::class.java)
            intent.putExtra(requireActivity().getString(R.string.navigation_from),requireActivity().getString(R.string.navigation_from_category))
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
            binding.textInputEditTextSearch.setText("")
        }
        binding.floatingActionButtonMainAdd.setOnClickListener {
            toggleFabMenu()
        }
        binding.extendedFabSettings.setOnClickListener {
            toggleFabMenu()
        }
        binding.floatingActionButtonSearch.setOnClickListener {
            if (binding.textInputLayoutSearch.isVisible) {
                editCategoryAdapter.categoriesList = viewModel.statusCategoriesList.value?.data?: listOf()
                binding.textInputLayoutSearch.hide()
            } else {
                binding.textInputLayoutSearch.show()
            }
            toggleFabMenu()
        }
    }

    private fun toggleFabMenu() {
        if (isFabMenuOpen) {
            binding.extendedFabSettings.hide()
            binding.floatingActionButtonSearch.hide()
            binding.floatingActionButtonAdd.hide()
            binding.floatingActionButtonMainAdd.show()
        } else {
            binding.extendedFabSettings.show()
            binding.floatingActionButtonSearch.show()
            binding.floatingActionButtonAdd.show()
            binding.floatingActionButtonMainAdd.hide()
        }
        isFabMenuOpen = !isFabMenuOpen
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}