package com.campuscoders.posterminalapp.presentation.edit.views

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import android.app.Activity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentUpdateOrAddProductBinding
import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.presentation.edit.UpdateOrAddProductViewModel
import com.campuscoders.posterminalapp.utils.Constants.CAMERA_REQUEST_CODE
import com.campuscoders.posterminalapp.utils.glide
import com.campuscoders.posterminalapp.utils.placeHolderProgressBar
import com.campuscoders.posterminalapp.utils.toCent
import com.campuscoders.posterminalapp.utils.toast
import java.io.File

class UpdateOrAddProductFragment: Fragment() {

    private var _binding: FragmentUpdateOrAddProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UpdateOrAddProductViewModel

    private var isAdd = true

    private var productFromDb: Products? = null

    private var productsCategoryId: String? = null

    private lateinit var currentPhotoPath: String

    private var currentPhotoUri: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateOrAddProductBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[UpdateOrAddProductViewModel::class.java]

        arguments?.let {bundle ->
            val productId = bundle.getString(requireActivity().getString(R.string.category_id_or_product_id))
            productsCategoryId = bundle.getString(requireActivity().getString(R.string.products_category_id))
            productId?.let {
                // viewModel.getProduct(it)
                isAdd = false
            }
        }

        binding.materialCardViewCamera.setOnClickListener {
            openCamera()
        }
        binding.materialCardViewRemove.setOnClickListener {
            currentPhotoUri = ""
            val drawable = ContextCompat.getDrawable(requireContext(),R.drawable.package_variant_closed_plus)
            binding.imageViewFromCamera.setImageDrawable(drawable)
        }
        binding.buttonAdd.setOnClickListener {
            if (areTheFieldsValid()) {
                if (isAdd) {
                    val product = getProduct()
                    //viewModel.addProduct(product)
                } else {
                    getChangedProduct()?.let {changedPrdct ->
                        //viewModel.updateProduct(changedPrdct)
                    }
                }
            }
        }

        observer()
    }

    private fun observer() {

    }

    private fun setProductInfos(product: Products) {
        binding.buttonAdd.text = "Güncelle"
        binding.textInputEditTextProductBarcode.setText(product.productBarcode)
        binding.textInputEditTextProductName.setText(product.productName)
        binding.textInputEditTextDescription.setText(product.productDescription)
        binding.textInputEditTextProductPrice.setText("${product.productPrice},${product.productPriceCents}")
        binding.imageViewFromCamera.glide(product.productImage, placeHolderProgressBar(requireContext()))
        binding.AutoCompleteTextViewKDV.setText(product.productKdv)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            try {
                val photoFile = createImageFile()
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(requireContext(),"com.campuscoders.posterminalapp.fileprovider",photoFile)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                    startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE)
                }
            } catch (e: Exception) {
                throw e.fillInStackTrace()
            }
        }
    }

    private fun createImageFile(): File? {
        val storageDir = requireActivity().getExternalFilesDir("images")
        val imageFileName = "JPEG_${System.currentTimeMillis()}.jpg"
        val imageFile = File(storageDir,imageFileName)
        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageFile = File(currentPhotoPath)
            currentPhotoUri = imageFile.toURI().toString()
            binding.imageViewFromCamera.glide(currentPhotoUri, placeHolderProgressBar(requireContext()))
        }
    }

    private fun getChangedProduct(): Products? {
        val splittedPrice = binding.textInputEditTextProductPrice.text.toString().split(",")
        val productPrice = splittedPrice[0]
        val productPriceCent = splittedPrice[1].toInt().toCent()
        productFromDb?.apply {
            this.productBarcode = binding.textInputEditTextProductBarcode.text.toString()
            this.productName = binding.textInputEditTextProductName.text.toString()
            this.productDescription = binding.textInputEditTextDescription.text.toString()
            this.productKdv = binding.AutoCompleteTextViewKDV.text.toString()
            this.productImage = currentPhotoUri
            this.productPrice = productPrice
            this.productPriceCents = productPriceCent
        }
        return productFromDb
    }

    private fun getProduct(): Products {
        val productBarcode = binding.textInputEditTextProductBarcode.text.toString()
        val productName = binding.textInputEditTextProductName.text.toString()
        val productDescription = binding.textInputEditTextDescription.text.toString()
        val productKdv = binding.AutoCompleteTextViewKDV.text.toString()
        val productImage = currentPhotoUri
        val splittedPrice = binding.textInputEditTextProductPrice.text.toString().split(",")
        val productPrice = splittedPrice[0]
        val productPriceCent = splittedPrice[1].toInt().toCent()
        return Products().apply {
            this.productCategoryId = productsCategoryId
            this.productBarcode = productBarcode
            this.productName = productName
            this.productDescription = productDescription
            this.productKdv = productKdv
            this.productImage = productImage
            this.productPrice = productPrice
            this.productPriceCents = productPriceCent
        }
    }

    private fun areTheFieldsValid(): Boolean {
        if (binding.textInputEditTextProductBarcode.text.toString() == "" ||
            binding.textInputEditTextProductName.text.toString() == "" ||
            binding.textInputEditTextDescription.text.toString() == "" ||
            binding.textInputEditTextProductPrice.text.toString() == "") {
            toast(requireContext(),requireActivity().getString(R.string.empty_fields),false)
            return false
        } else if (currentPhotoUri == "") {
            toast(requireContext(),requireActivity().getString(R.string.camera_product),false)
            return false
        } else if (binding.textInputEditTextProductPrice.text.toString().contains(",")) {
            toast(requireContext(),requireActivity().getString(R.string.warn_price),false)
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}