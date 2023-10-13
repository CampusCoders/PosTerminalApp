package com.campuscoders.posterminalapp.presentation.cancel_and_document.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.campuscoders.posterminalapp.databinding.FragmentDocumentDetailsBinding
import com.campuscoders.posterminalapp.presentation.cancel_and_document.BaseViewModel
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show

class DocumentDetailsFragment: Fragment() {

    private var _binding: FragmentDocumentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDocumentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[BaseViewModel::class.java]

        arguments?.let {
            val from = it.getString("from")
            if (from == "cancel_sale") {
                binding.linearLayoutDocument.hide()
                binding.linearLayoutCancel.show()
            } else {
                binding.linearLayoutCancel.hide()
                binding.linearLayoutDocument.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}