package com.campuscoders.posterminalapp.presentation.cashier_and_daily_report.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.recyclerViewDailyReport.adapter = dailyReportAdapter
        binding.recyclerViewDailyReport.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        observer()
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
            binding.textViewKDV.text = it
        }
        viewModel.statusSumOfOrdersPrice.observe(viewLifecycleOwner) {
            binding.textViewTotal.text = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}