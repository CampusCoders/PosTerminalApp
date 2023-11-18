package com.campuscoders.posterminalapp.presentation.edit.views

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentUpdateOrAddCategoryBinding
import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.presentation.edit.UpdateOrAddCategoryViewModel
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.glide
import com.campuscoders.posterminalapp.utils.placeHolderProgressBar
import com.campuscoders.posterminalapp.utils.toast
import java.io.File
import java.io.IOException

class UpdateOrAddCategoryFragment: Fragment() {

    private var _binding: FragmentUpdateOrAddCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UpdateOrAddCategoryViewModel

    private val CAMERA_REQUEST_CODE = 1

    private lateinit var currentPhotoPath: String

    private var currentPhotoUri: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateOrAddCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[UpdateOrAddCategoryViewModel::class.java]

        arguments?.let {
            val categoryId = it.getString(requireActivity().getString(R.string.category_id_or_product_id))
            categoryId?.let { ctgryId ->
                viewModel.getCategory(ctgryId)
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
                // viewModel.someMethod(getValues())
            }
        }

        observer()
    }

    private fun observer() {
        viewModel.statusFetchedCategory.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let {category ->
                        setCategoryInfos(category)
                    }
                }
                is Resource.Loading -> {
                    // loading popup or progressbar
                }
                is Resource.Error -> {
                    toast(requireContext(),it.message?:"Error!",false)
                }
            }
        }
    }

    private fun setCategoryInfos(category: Categories) {
        binding.textInputEditTextCategoryCode.setText(category.categoryCode)
        binding.textInputEditTextCategoryName.setText(category.categoryName)
        binding.textInputEditTextDescription.setText(category.categoryDescription)
        binding.imageViewFromCamera.glide(category.categoryImage, placeHolderProgressBar(requireContext()))
        currentPhotoUri = category.categoryImage?:""
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
            } catch (e: IOException) {
                throw e.fillInStackTrace()
            }
        }
    }

    private fun createImageFile(): File? {
        val storageDir = requireActivity().getExternalFilesDir("images")
        val imageFileName = "JPEG_${System.currentTimeMillis()}.jpg"
        val image = File(storageDir,imageFileName)
        currentPhotoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageFile = File(currentPhotoPath)

            // to get image file's uri
            currentPhotoUri = imageFile.toURI().toString()
            binding.imageViewFromCamera.glide(currentPhotoUri, placeHolderProgressBar(requireContext()))

            // currentPhotoPath -> /storage/emulated/0/Android/data/com.campuscoders.posterminalapp/files/images/JPEG_1694090102822.jpg
            // urrentPhotoUri   -> file:/storage/emulated/0/Android/data/com.campuscoders.posterminalapp/files/images/JPEG_1694090102822.jpg
        }
    }

    private fun getValues(): Categories {
        val categoryCode = binding.textInputEditTextCategoryCode.text.toString()
        val categoryName = binding.textInputEditTextCategoryName.text.toString()
        val categoryDescription = binding.textInputEditTextDescription.text.toString()
        val categoryImage = currentPhotoUri
        return Categories(categoryCode,categoryName,categoryDescription, categoryImage)
    }

    private fun areTheFieldsValid(): Boolean {
        if (binding.textInputEditTextCategoryCode.text.toString() == "" || binding.textInputEditTextCategoryName.text.toString() == "" || binding.textInputEditTextDescription.text.toString() == "") {
            toast(requireContext(),requireActivity().getString(R.string.empty_fields),false)
            return false
        } else if (currentPhotoUri == ""){
            toast(requireContext(),requireActivity().getString(R.string.camera_category),false)
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}