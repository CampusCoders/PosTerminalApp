package com.campuscoders.posterminalapp.presentation.cashier_and_daily_report.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentDailyReportBinding
import com.campuscoders.posterminalapp.presentation.cashier_and_daily_report.DailyReportViewModel
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.hide
import com.campuscoders.posterminalapp.utils.show
import com.campuscoders.posterminalapp.utils.toast

class DailyReportFragment : Fragment() {

    private var _binding: FragmentDailyReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DailyReportViewModel

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

        viewModel.fetchOrders(null,null,null, requireContext())

        setDropDownMenus()

        binding.recyclerViewDailyReport.adapter = dailyReportAdapter
        binding.recyclerViewDailyReport.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        observer()
    }

    private fun setDropDownMenus() {
        val optionsStatus = arrayOf(getString(R.string.adapter_all_result), getString(R.string.adapter_successful), getString(R.string.adapter_basket_cancel), getString(R.string.adapter_cancel_sale))
        val optionsDocument = arrayOf(getString(R.string.adapter_all_documents), getString(R.string.adapter_e_receipt), getString(R.string.adapter_e_archive_receipt))
        // val optionsDate = arrayOf(getString(R.string.adapter_document_no), getString(R.string.adapter_mali_id), getString(R.string.adapter_terminal_id))
        val arrayAdapterStatus = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line,optionsStatus)
        val arrayAdapterDocument = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line,optionsDocument)
        // val arrayAdapterDate = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line,optionsDate)

        binding.autoCompleteTextViewStatus.setAdapter(arrayAdapterStatus)
        binding.autoCompleteTextViewStatus.setText(getString(R.string.adapter_all_result),false)
        binding.autoCompleteTextViewStatus.setOnItemClickListener { _, _, _, _ ->
            viewModel.fetchOrders(binding.textInputLayoutStatus.editText?.text.toString(),binding.textInputLayoutDocuments.editText?.text.toString(),null, requireContext())
        }

        binding.autoCompleteTextViewDocuments.setAdapter(arrayAdapterDocument)
        binding.autoCompleteTextViewDocuments.setText(getString(R.string.adapter_all_documents),false)
        binding.autoCompleteTextViewDocuments.setOnItemClickListener { _, _, _, _ ->
            viewModel.fetchOrders(binding.textInputLayoutStatus.editText?.text.toString(),binding.textInputLayoutDocuments.editText?.text.toString(),null, requireContext())
        }
    }

    private fun observer() {
        viewModel.statusOrderList.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarDailyReport.hide()
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
        viewModel.statusSumOfOrdersTax.observe(viewLifecycleOwner) {
            //binding.textViewKDV.text = it
        }
        viewModel.statusSumOfOrdersPrice.observe(viewLifecycleOwner) {
            //binding.textViewTotal.text = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}