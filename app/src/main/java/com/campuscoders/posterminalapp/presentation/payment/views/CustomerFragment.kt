package com.campuscoders.posterminalapp.presentation.payment.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentCustomerBinding
import com.campuscoders.posterminalapp.presentation.payment.CustomerViewModel
import com.campuscoders.posterminalapp.presentation.sale.views.ShoppingCartItems
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.TimeAndDate
import com.campuscoders.posterminalapp.utils.toast
import kotlin.random.Random

class CustomerFragment : Fragment() {

    private var _binding: FragmentCustomerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CustomerViewModel

    private var ftransaction: FragmentTransaction? = null

    private var orderReceiptType = "E-Arşiv Fatura"

    private var orderCancel = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomerInfos()

        viewModel = ViewModelProvider(requireActivity())[CustomerViewModel::class.java]
        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        binding.card1EArsivBill.setOnClickListener {
            orderReceiptType = "E-Arşiv Fatura"

            binding.card1EArsivBill.setCardBackgroundColor(resources.getColor(R.color.background))
            binding.card2EBill.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.textViewCard1EArsivBill.setTextColor(resources.getColor(R.color.white))
            binding.textViewCard2EBill.setTextColor(resources.getColor(R.color.background))
        }
        binding.card2EBill.setOnClickListener {
            orderReceiptType = "E-Fatura"

            binding.card1EArsivBill.setCardBackgroundColor(resources.getColor(R.color.white))
            binding.card2EBill.setCardBackgroundColor(resources.getColor(R.color.background))
            binding.textViewCard1EArsivBill.setTextColor(resources.getColor(R.color.background))
            binding.textViewCard2EBill.setTextColor(resources.getColor(R.color.white))
        }

        binding.buttonContinue.setOnClickListener {
            val orderReceiptNo = createRandomOrderReceiptNo()
            val orderId = ShoppingCartItems.getOrderId()
            val date = TimeAndDate.getLocalDate(Constants.DATE_FORMAT)
            val time = TimeAndDate.getTime()
            val orderTotal = ShoppingCartItems.getTotalPrice()
            val orderTotalTax = ShoppingCartItems.getTotalTax()
            ShoppingCartItems.setOrderNo(orderReceiptNo)
            ShoppingCartItems.setDate(date)
            ShoppingCartItems.setTime(time)
            viewModel.updateOrder(
                orderReceiptType,
                date,
                time,
                "Successful",
                orderReceiptNo,
                orderId,
                orderTotal,
                orderTotalTax
            )
        }

        binding.buttonCancel.setOnClickListener {
            orderCancel = true
            val orderReceiptNo = createRandomOrderReceiptNo()
            val orderId = ShoppingCartItems.getOrderId()
            val date = TimeAndDate.getLocalDate(Constants.DATE_FORMAT)
            val time = TimeAndDate.getTime()
            val orderTotal = ShoppingCartItems.getTotalPrice()
            val orderTotalTax = ShoppingCartItems.getTotalTax()
            ShoppingCartItems.setDate(date)
            ShoppingCartItems.setTime(time)
            ShoppingCartItems.setOrderNo(orderReceiptNo)
            viewModel.updateOrder(
                orderReceiptType,
                date,
                time,
                "Basket Cancel",
                orderReceiptNo,
                orderId,
                orderTotal,
                orderTotalTax
            )
            toast(requireContext(), requireActivity().getString(R.string.order_cancel), false)
            requireActivity().finish()
        }

        // if user clicked to back button then user will go MainActivity, Order_status -> cancel
        observer()
    }

    private fun observer() {
        viewModel.statusUpdateOrder.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (!orderCancel) {
                        toast(
                            requireContext(),
                            requireActivity().getString(R.string.order_successful),
                            false
                        )
                        ftransaction?.let { ft ->
                            ft.replace(
                                R.id.fragmentContainerViewPaymentActivity,
                                SelectCartTypeFragment()
                            )
                            ft.commit()
                        }
                    }
                }

                is Resource.Loading -> {
                    // loading dialog
                }

                is Resource.Error -> {
                    toast(requireContext(), it.message ?: "Error!", false)
                }
            }
        }
    }

    private fun createRandomOrderReceiptNo(): String {
        val randomNumber = Random.nextInt(100000, 999999)
        return "SAS$randomNumber"
    }

    private fun setCustomerInfos() {
        binding.textInputEditTextTCKN.setText(Constants.CUSTOMER_VKN_TCKN)
        binding.textInputEditTextCustomerName.setText(ShoppingCartItems.getCustomerName())
        binding.textInputEditTextCustomerSurname.setText(Constants.CUSTOMER_LAST_NAME)
        binding.textInputEditTextPhone.setText(Constants.CUSTOMER_PHONE_NUMBER)
        binding.textInputEditTextMail.setText(Constants.CUSTOMER_EMAIL)
        binding.AutoCompleteTextViewCity.setText(Constants.CUSTOMER_PROVINCE)
        binding.AutoCompleteTextViewDistrict.setText(Constants.CUSTOMER_DISTRICT)
        binding.textInputEditTextAddress.setText(Constants.CUSTOMER_ADDRESS)
        binding.textInputEditTextPaymentType.setText(ShoppingCartItems.getPaymentType())
        binding.textInputEditTextTotal.setText(ShoppingCartItems.getTotalPrice())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}