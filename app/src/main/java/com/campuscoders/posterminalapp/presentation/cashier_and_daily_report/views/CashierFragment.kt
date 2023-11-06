package com.campuscoders.posterminalapp.presentation.cashier_and_daily_report.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentCashierListBinding
import com.campuscoders.posterminalapp.presentation.cashier_and_daily_report.CashierViewModel
import com.campuscoders.posterminalapp.utils.CustomSharedPreferences
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CashierFragment : Fragment() {

    private var _binding: FragmentCashierListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CashierViewModel

    private var ftransaction: FragmentTransaction? = null

    private var terminalId = 0

    private var terminalUser = hashMapOf<String,Any>()

    private val cashierAdapter by lazy {
        CashierAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCashierListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customSharedPreferences = CustomSharedPreferences(requireContext())
        terminalUser = customSharedPreferences.getTerminalUserLogin()

        viewModel = ViewModelProvider(requireActivity())[CashierViewModel::class.java]
        ftransaction = requireActivity().supportFragmentManager.beginTransaction()

        viewModel.getAllCashiers()

        val manager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewCashier.adapter = cashierAdapter
        binding.recyclerViewCashier.layoutManager = manager

        cashierAdapter.setOnLongItemClickListener {
            terminalId = it
            showEditOrDeletePopup(terminalId)
        }

        binding.buttonAddCashier.setOnClickListener {
            if (terminalUser["kasiyer_ekleme_duzenleme"] as Boolean) {
                ftransaction?.let {
                    it.replace(R.id.fragmentContainerViewCashierActivity, AddCashierFragment())
                    it.addToBackStack(null)
                    it.commit()
                }
            } else {
                toast(requireContext(),"Yetkiniz yok.",false)
            }
        }

        observer()
    }

    private fun observer() {
        viewModel.statusFetchedCashiersList.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    cashierAdapter.cashiersList = it.data?: arrayListOf()
                }
                is Resource.Loading -> {
                    // loading popup
                }
                is Resource.Error -> {
                    toast(requireContext(),it.message?:"Error",false)
                }
            }
        }
        viewModel.statusDeleteCashier.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    cashierAdapter.updateCashierList(terminalId)
                }
                is Resource.Loading -> {
                    // loading popup
                }
                is Resource.Error -> {
                    toast(requireContext(),it.message?:"Error",false)
                }
            }
        }
    }

    private fun showEditOrDeletePopup(terminalId: Int) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.pop_up_edit_or_delete_item,null)
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(dialogView).create()

        val linearEdit = dialogView.findViewById<LinearLayout>(R.id.linearLayoutEdit)
        val linearDelete = dialogView.findViewById<LinearLayout>(R.id.linearLayoutDelete)

        linearEdit.setOnClickListener {
            if (terminalUser["kasiyer_ekleme_duzenleme"] as Boolean) {
                val addCashierFragment = AddCashierFragment()
                val bundle = Bundle()
                bundle.putInt("terminal_id",terminalId)
                addCashierFragment.arguments = bundle
                ftransaction?.let {
                    it.replace(R.id.fragmentContainerViewCashierActivity, addCashierFragment)
                    it.addToBackStack(null)
                    it.commit()
                }
            } else {
                toast(requireContext(),"Yetkiniz yok.",false)
            }

            dialog.dismiss()
        }

        linearDelete.setOnClickListener {
            if (terminalUser["kasiyer_silme"] as Boolean) {
                viewModel.deleteCashier(terminalId)
            } else {
                toast(requireContext(),"Yetkiniz yok.",false)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}