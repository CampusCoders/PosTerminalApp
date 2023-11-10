package com.campuscoders.posterminalapp.presentation.edit.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentServicesBinding
import com.campuscoders.posterminalapp.presentation.UpdateOrAddActivity
import com.campuscoders.posterminalapp.presentation.sale.views.BarcodeScannerActivity

class ServicesFragment : Fragment() {

    private var _binding: FragmentServicesBinding? = null
    private val binding get() = _binding!!

    private var isFabMenuOpen: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentServicesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            intent.putExtra("from", "product")
            intent.putExtra("category_or_product_id", -1)
            startActivity(intent)
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