package com.campuscoders.posterminalapp.presentation.cancel_and_document.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentDocumentDetailsBinding
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.presentation.cancel_and_document.BaseViewModel
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.placeHolderProgressBar
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class DocumentDetailsFragment: Fragment() {

    private var _binding: FragmentDocumentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BaseViewModel

    private lateinit var ftransaction: androidx.fragment.app.FragmentManager

    private lateinit var cost: String

    private lateinit var documentNo: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDocumentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ftransaction = requireActivity().supportFragmentManager

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

        // Cancel Sale
        binding.buttonBackCancel.setOnClickListener {
            ftransaction.popBackStack()
        }
        binding.buttonCancelSale.setOnClickListener {
            viewModel.cancelSale()
            showCancelSalePopUp()
        }

        // Query E-Document
        binding.buttonBack.setOnClickListener {
            ftransaction.popBackStack()
        }
        binding.buttonShow.setOnClickListener {
            toast(requireContext(),"Coming soon",false)
        }
        binding.buttonSend.setOnClickListener {
            showCustomerInfoPopUp()
        }

        observe()
    }

    private fun observe() {
        viewModel.statusOrderDetail.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    viewModel.fetchOrdersProductsList()
                    setOrderDetail(it.data!!)
                    documentNo = it.data.orderReceiptNo!!
                }
                is Resource.Loading -> {}
                is Resource.Error -> {
                    toast(requireContext(),it.message?:requireActivity().getString(R.string.couldnt_get_order),false)
                }
            }
        }
        viewModel.statusProductAndTaxPrice.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.textViewTotalAmount.text = "₺${it.data!!["price"]}"
                    binding.textViewTotalTax.text = "₺${it.data["tax"]}"
                    cost = it.data["price"].toString()
                }
                is Resource.Loading -> {}
                is Resource.Error -> {
                    toast(requireContext(),it.message?:requireActivity().getString(R.string.couldnt_get_order),false)
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

    private fun showCancelSalePopUp() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.pop_up_cancel_sale,null)
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(dialogView).setCancelable(false).create()

        val buttonOk = dialogView.findViewById<Button>(R.id.buttonOk)
        val imageViewCheckCost = dialogView.findViewById<ImageView>(R.id.imageViewCheckCost)
        val imageViewCheckProcess = dialogView.findViewById<ImageView>(R.id.imageViewCheckProcess)
        val textViewCancelSale = dialogView.findViewById<TextView>(R.id.textViewCancelSale)
        val textViewSaleTotal = dialogView.findViewById<TextView>(R.id.textViewSaleTotal)
        val textViewOrderNo = dialogView.findViewById<TextView>(R.id.textViewOrderNo)

        buttonOk.setOnClickListener {
            dialog.dismiss()
            ftransaction.popBackStack()
        }

        viewModel.statusCancelSale.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    imageViewCheckCost.setImageDrawable(resources.getDrawable(R.drawable.check_circle))
                    imageViewCheckProcess.setImageDrawable(resources.getDrawable(R.drawable.check_circle))
                    textViewCancelSale.text = "E-Belge iptal edildi"
                    textViewSaleTotal.text = cost
                    textViewOrderNo.text = documentNo
                }
                is Resource.Loading -> {
                    textViewCancelSale.text = "E-Belge iptal ediliyor"
                    imageViewCheckCost.setImageDrawable(placeHolderProgressBar(requireContext()))
                    imageViewCheckProcess.setImageDrawable(placeHolderProgressBar(requireContext()))
                }
                is Resource.Error -> {
                    textViewCancelSale.text = "E-Belge iptal edilemedi!"
                    imageViewCheckCost.setImageDrawable(resources.getDrawable(R.drawable.checkerror))
                    imageViewCheckProcess.setImageDrawable(resources.getDrawable(R.drawable.checkerror))
                }
            }
        }

        dialog.show()
    }

    private fun showCustomerInfoPopUp() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.pop_up_customer_info,null)
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(dialogView).setCancelable(false).create()

        val phoneNumber = dialogView.findViewById<TextInputEditText>(R.id.textInputEditTextPhone)
        val email = dialogView.findViewById<TextInputEditText>(R.id.textInputEditTextEmail)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
        val buttonOk = dialogView.findViewById<Button>(R.id.buttonOk)

        buttonOk.setOnClickListener {
            if (phoneNumber.text.toString() == "" && email.text.toString() == "") {
                toast(requireContext(),requireActivity().getString(R.string.customer_information),false)
            } else {
                dialog.dismiss()
                ftransaction.popBackStack()
            }
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}