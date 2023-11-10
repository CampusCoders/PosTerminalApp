package com.campuscoders.posterminalapp.presentation.edit.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.campuscoders.posterminalapp.databinding.FragmentServicesBinding
import com.campuscoders.posterminalapp.utils.toast

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

        setFabMenu()
    }

    private fun setFabMenu() {
        binding.extendedFabSettings.hide()
        binding.floatingActionButtonSearch.hide()
        binding.floatingActionButtonAdd.hide()

        binding.floatingActionButtonAdd.setOnClickListener {
            toast(requireContext(),"Coming soon...",false)
        }
        binding.floatingActionButtonMainAdd.setOnClickListener {
            toggleFabMenu()
        }
        binding.extendedFabSettings.setOnClickListener {
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