package com.campuscoders.posterminalapp.presentation.edit.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.campuscoders.posterminalapp.databinding.FragmentUpdateOrAddProductBinding

class UpdateOrAddProductFragment: Fragment() {

    private var _binding: FragmentUpdateOrAddProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateOrAddProductBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val productId = it.getInt("category_or_product_id")
            if (productId != -1) {
                // ilgili product'ın id'si ile veriler ekrana basılacak
                // viewModel.getProduct()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}