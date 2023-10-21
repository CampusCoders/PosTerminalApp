package com.campuscoders.posterminalapp.presentation.cancel_and_document.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentCancelSaleAndQueryDocumentBinding
import com.campuscoders.posterminalapp.presentation.cancel_and_document.BaseViewModel
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.showProgressDialog
import com.campuscoders.posterminalapp.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CancelSaleAndQueryDocumentFragment : Fragment() {

    private var _binding: FragmentCancelSaleAndQueryDocumentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BaseViewModel

    private var ftransaction: FragmentTransaction? = null

    private var from: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCancelSaleAndQueryDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[BaseViewModel::class.java]

        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        setDropDownMenu()

        arguments?.let {
            from = it.getString("from")
            if (from == "cancel_sale") {
                binding.topAppBar.title = "Satış İptal"
            } else {
                binding.topAppBar.title = "E-Belge Sorgulama"
            }
        }

        binding.buttonQuerySale.setOnClickListener {
            context?.showProgressDialog(Constants.QUERY_SALE)
            Handler(Looper.getMainLooper()).postDelayed({

                val searchType = binding.textInputLayoutSearchType.editText?.text.toString()
                val searchKey = binding.textInputLayoutValue.editText?.text.toString()

                viewModel.querySale(searchType,searchKey,requireContext())

                val documentDetailsFragment = DocumentDetailsFragment()
                val bundle = Bundle()
                if (from == "cancel_sale") {
                    bundle.putString("from","cancel_sale")
                } else {
                    bundle.putString("from","query_document")
                }
                documentDetailsFragment.arguments = bundle
                ftransaction?.let {
                    it.replace(R.id.fragmentContainerViewCancelSaleEDocument,documentDetailsFragment)
                    it.addToBackStack(null)
                    it.commit()
                }
            }, Constants.PROGRESS_BAR_DURATION.toLong())
        }

        binding.buttonFetchLastSuccess.setOnClickListener {
            viewModel.fetchLatestSuccessfulSale()
        }

        observer()
    }

    private fun observer() {
        viewModel.statusOrderDetail.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.textInputEditTextPDate.setText(it.data?.orderDate)
                    when(binding.autoCompleteTextViewSearchType.text.toString()) {
                        getString(R.string.adapter_document_no) -> {
                            binding.textInputEditTextValue.setText(it.data?.orderReceiptNo)
                        }
                        getString(R.string.adapter_mali_id) -> {
                            binding.textInputEditTextValue.setText(it.data?.orderMaliId)
                        }
                        getString(R.string.adapter_terminal_id) -> {
                            binding.textInputEditTextValue.setText(it.data?.orderTerminalId)
                        }
                    }
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    toast(requireContext(),it.message?:"Error",false)
                }
            }
        }
    }

    private fun setDropDownMenu() {
        val options = arrayOf(getString(R.string.adapter_document_no), getString(R.string.adapter_mali_id), getString(R.string.adapter_terminal_id))
        val arrayAdapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line,options)
        binding.autoCompleteTextViewSearchType.setAdapter(arrayAdapter)
        binding.autoCompleteTextViewSearchType.setText(getString(R.string.adapter_document_no), false)
        binding.autoCompleteTextViewSearchType.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLayoutSearchType.hint = options[i]
            binding.textInputLayoutValue.hint = options[i]
            binding.textInputEditTextValue.setText("")
            binding.textInputEditTextPDate.setText("")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}