package com.campuscoders.posterminalapp.presentation.cancel_and_document.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.campuscoders.posterminalapp.databinding.FragmentDocumentDetailsBinding
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.presentation.cancel_and_document.BaseViewModel
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toast

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

        observe()
    }

    private fun observe() {
        viewModel.statusOrderDetail.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    viewModel.fetchOrdersProductsList()
                    setOrderDetail(it.data!!)
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    toast(requireContext(),it.message?:"Error Order!",false)
                }
            }
        }
        viewModel.statusProductAndTaxPrice.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.textViewTotalAmount.text = it.data!!["price"]
                    binding.textViewTotalTax.text = it.data["tax"]
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    toast(requireContext(),it.message?:"Error Order!",false)
                }
            }
        }
    }

    private fun setOrderDetail(order: Orders) {
        binding.textViewUyeNo.text = order.orderUyeIsyeriNo
        binding.textViewTerminalNo.text = order.orderTerminalId
        binding.textViewETTN.text = order.orderETTN
        binding.textViewDocumentNo.text = order.orderReceiptNo
        binding.textViewDocumentType.text = order.orderReceiptType
        binding.textViewMaliID.text = order.orderMaliId
        binding.textViewOrderNo.text = order.orderOrderNoBackend
        binding.textViewBillTime.text = order.orderTime
        binding.textViewBillDate.text = order.orderDate
        binding.textViewPaymentDetail.text = order.orderPaymentType
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}