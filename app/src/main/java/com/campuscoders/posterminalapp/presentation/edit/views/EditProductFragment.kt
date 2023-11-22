package com.campuscoders.posterminalapp.presentation.edit.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentEditProductBinding
import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.presentation.UpdateOrAddActivity
import com.campuscoders.posterminalapp.presentation.edit.EditProductViewModel
import com.campuscoders.posterminalapp.presentation.sale.views.BarcodeScannerActivity
import com.campuscoders.posterminalapp.utils.CustomSharedPreferences
import com.campuscoders.posterminalapp.utils.FilterList
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.glide
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.placeHolderProgressBar
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toCent
import com.campuscoders.posterminalapp.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditProductFragment : Fragment() {

    private var _binding: FragmentEditProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EditProductViewModel

    private var ftransaction: FragmentTransaction? = null

    private lateinit var customSharedPreferences: CustomSharedPreferences

    private var isFabMenuOpen: Boolean = false

    private var productId = 0

    private lateinit var productCategoryId: String

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.getProductsByCategoryId(productCategoryId)
        }
    }

    private val editProductAdapter by lazy {
        EditProductAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customSharedPreferences = CustomSharedPreferences(requireContext())
        val terminalUser = customSharedPreferences.getTerminalUserLogin()

        viewModel = ViewModelProvider(requireActivity())[EditProductViewModel::class.java]

        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        var job: Job? = null

        binding.textInputLayoutSearch.hide()

        binding.textInputEditTextSearch.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        editProductAdapter.productsList = FilterList.bySearch(viewModel.statusProductsList.value?.data?: listOf(),it.toString())
                    } else {
                        editProductAdapter.productsList = viewModel.statusProductsList.value?.data?: listOf()
                    }
                    editProductAdapter.notifyDataSetChanged()
                }
            }
        }

        arguments?.let {
            productCategoryId = it.getString("categoryId")?:"-1"
            viewModel.getProductsByCategoryId(productCategoryId)
        }

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        binding.recyclerViewEditProduct.adapter = editProductAdapter
        binding.recyclerViewEditProduct.layoutManager = staggeredGridLayoutManager

        editProductAdapter.setOnItemClickListener {
            if (terminalUser["urun_goruntuleme"] as Boolean) {
                showProductDetailPopup(it)
            } else {
                toast(requireContext(),"Yetkiniz yok.",false)
            }
        }

        editProductAdapter.setOnLongItemClickListener {
            if (terminalUser["urun_ekleme_duzenleme"] as Boolean) {
                showEditOrDeletePopup(it, terminalUser["urun_silme"] as Boolean)
                productId = it
            } else {
                toast(requireContext(),"Yetkiniz yok.",false)
            }
        }

        setFabMenu()

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
                    toast(requireContext(), "Ürün silindi.", false)
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
    }

    private fun showProductDetailPopup(product: Products) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.pop_up_product_details,null)
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(dialogView).create()

        val productImage = dialogView.findViewById<ImageView>(R.id.imageViewProductImage)
        val productName = dialogView.findViewById<TextView>(R.id.textViewProductName)
        val productTotal = dialogView.findViewById<TextView>(R.id.textViewTotal)
        val productTax = dialogView.findViewById<TextView>(R.id.textViewTax)
        val productBarcode = dialogView.findViewById<TextView>(R.id.textViewBarcode)
        val buttonOk = dialogView.findViewById<Button>(R.id.buttonOk)

        productImage.glide(product.productImage?:"", placeHolderProgressBar(requireContext()))
        productName.text = product.productName
        productTotal.text = "₺${product.productPrice},${product.productPriceCents?.toInt()?.toCent()}"
        productTax.text = "%${product.productKdv}"
        productBarcode.text = product.productBarcode

        buttonOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditOrDeletePopup(productId: Int, deleteProduct: Boolean) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.pop_up_edit_or_delete_item, null)
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(dialogView).create()

        val linearEdit = dialogView.findViewById<LinearLayout>(R.id.linearLayoutEdit)
        val linearDelete = dialogView.findViewById<LinearLayout>(R.id.linearLayoutDelete)

        linearEdit.setOnClickListener {
            val intent = Intent(requireActivity(), UpdateOrAddActivity::class.java)
            intent.putExtra(requireActivity().getString(R.string.navigation_from), requireActivity().getString(R.string.navigation_from_product))
            intent.putExtra(requireActivity().getString(R.string.category_id_or_product_id), productId.toString())
            intent.putExtra(requireActivity().getString(R.string.products_category_id), productCategoryId)
            launcher.launch(intent)
            requireActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
            dialog.dismiss()
        }

        linearDelete.setOnClickListener {
            if (deleteProduct) {
                viewModel.deleteProduct(productId)
            } else {
                toast(requireContext(),"Yetkiniz yok.",false)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setFabMenu() {
        binding.extendedFabSettings.hide()
        binding.floatingActionButtonSearch.hide()
        binding.floatingActionButtonBarcode.hide()
        binding.floatingActionButtonAdd.hide()

        binding.floatingActionButtonBarcode.setOnClickListener {
            val intent = Intent(requireActivity(), BarcodeScannerActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }
        binding.floatingActionButtonAdd.setOnClickListener {
            val intent = Intent(requireActivity(), UpdateOrAddActivity::class.java)
            intent.putExtra(requireActivity().getString(R.string.navigation_from), requireActivity().getString(R.string.navigation_from_product))
            intent.putExtra(requireActivity().getString(R.string.products_category_id), productCategoryId)
            launcher.launch(intent)
            requireActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }
        binding.floatingActionButtonMainAdd.setOnClickListener {
            toggleFabMenu()
        }
        binding.floatingActionButtonBack.setOnClickListener {

        }
        binding.extendedFabSettings.setOnClickListener {
            toggleFabMenu()
        }
        binding.floatingActionButtonSearch.setOnClickListener {
            if (binding.textInputLayoutSearch.isVisible) {
                editProductAdapter.productsList = viewModel.statusProductsList.value?.data?: listOf()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}