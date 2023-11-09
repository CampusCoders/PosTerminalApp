package com.campuscoders.posterminalapp.presentation.cashier_and_daily_report.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentDailyReportBinding
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.presentation.cashier_and_daily_report.DailyReportViewModel
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toCent
import com.campuscoders.posterminalapp.utils.toast

class DailyReportFragment : Fragment() {

    private var _binding: FragmentDailyReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DailyReportViewModel

    private var ftransaction: FragmentTransaction? = null

    private val dailyReportAdapter by lazy {
        DailyReportAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDailyReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[DailyReportViewModel::class.java]

        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        viewModel.fetchOrders(null,null,null, requireContext())

        binding.materialCardViewDocumentSearch.setOnClickListener {
            ftransaction?.let {
                it.replace(R.id.fragmentContainerViewCashierAndDailyReportActivity,CalendarFragment())
                it.commit()
            }
        }

        setDropDownMenus()

        binding.recyclerViewDailyReport.adapter = dailyReportAdapter
        binding.recyclerViewDailyReport.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        observer()
    }

    private fun setDropDownMenus() {
        val optionsStatus = arrayOf(getString(R.string.adapter_all_result), getString(R.string.adapter_successful), getString(R.string.adapter_basket_cancel), getString(R.string.adapter_cancel_sale))
        val optionsDocument = arrayOf(getString(R.string.adapter_all_documents), getString(R.string.adapter_e_receipt), getString(R.string.adapter_e_archive_receipt))
        val optionsDate = arrayOf(getString(R.string.adapter_today), getString(R.string.adapter_yesterday), getString(R.string.adapter_last_week), getString(R.string.adapter_last_month))
        val arrayAdapterStatus = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line,optionsStatus)
        val arrayAdapterDocument = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line,optionsDocument)
        val arrayAdapterDate = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line,optionsDate)

        binding.autoCompleteTextViewStatus.setAdapter(arrayAdapterStatus)
        binding.autoCompleteTextViewStatus.setText(getString(R.string.adapter_all_result),false)
        binding.autoCompleteTextViewStatus.setOnItemClickListener { _, _, _, _ ->
            viewModel.fetchOrders(binding.textInputLayoutStatus.editText?.text.toString(),binding.textInputLayoutDocuments.editText?.text.toString(),
                binding.textInputLayoutDate.editText?.text.toString(), requireContext())
        }

        binding.autoCompleteTextViewDocuments.setAdapter(arrayAdapterDocument)
        binding.autoCompleteTextViewDocuments.setText(getString(R.string.adapter_all_documents),false)
        binding.autoCompleteTextViewDocuments.setOnItemClickListener { _, _, _, _ ->
            viewModel.fetchOrders(binding.textInputLayoutStatus.editText?.text.toString(),binding.textInputLayoutDocuments.editText?.text.toString(),
                binding.textInputLayoutDate.editText?.text.toString(), requireContext())
        }

        binding.autoCompleteTextViewDate.setAdapter(arrayAdapterDate)
        binding.autoCompleteTextViewDate.setText(getString(R.string.adapter_today),false)
        binding.autoCompleteTextViewDate.setOnItemClickListener { _, _, _, _ ->
            viewModel.fetchOrders(binding.textInputLayoutStatus.editText?.text.toString(),binding.textInputLayoutDocuments.editText?.text.toString(),
                binding.textInputLayoutDate.editText?.text.toString(), requireContext())
        }
    }

    private fun observer() {
        viewModel.statusOrderList.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarDailyReport.hide()
                    binding.textViewQuantity.text = it.data?.size.toString() + " Adet"
                    setCalculations(it.data?: listOf())
                    dailyReportAdapter.updateOrderList(it.data?: listOf())
                }
                is Resource.Loading -> {
                    binding.progressBarDailyReport.show()
                }
                is Resource.Error -> {
                    binding.progressBarDailyReport.hide()
                    toast(requireContext(),"Hata! Kayıtlar getirilemedi",false)
                }
            }
        }
    }

    private fun setCalculations(list: List<Orders>) {
        var totalPrice = 0
        var totalPriceCent = 0
        var totalTax = 0
        var totalTaxCent = 0

        for (i in list) {
            val splittedTotal = i.orderTotal?.split(",")
            val splittedTotalTax = i.orderTotalTax?.split(",")
            totalPrice += splittedTotal!![0].toInt()
            totalPriceCent += splittedTotal[1].toInt()
            totalTax += splittedTotalTax!![0].toInt()
            totalTaxCent += splittedTotalTax[1].toInt()
        }
        totalPrice += totalPriceCent / 100
        totalPriceCent %= 100
        totalTax += totalTaxCent / 100
        totalTaxCent %= 100

        binding.textViewTotalPrice.text = "₺$totalPrice,${totalPriceCent.toCent()}"
        binding.textViewTotalTax.text = "₺$totalTax,${totalTaxCent.toCent()}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}