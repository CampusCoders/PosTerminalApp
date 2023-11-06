package com.campuscoders.posterminalapp.presentation.sale.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.databinding.FragmentShoppingCartBinding
import com.campuscoders.posterminalapp.presentation.PaymentActivity
import com.campuscoders.posterminalapp.presentation.SaleActivity
import com.campuscoders.posterminalapp.presentation.sale.BaseViewModel
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.showProgressDialog
import com.campuscoders.posterminalapp.utils.toast

class ShoppingCartFragment : Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var baseViewModel: BaseViewModel

    private lateinit var saleActivity: SaleActivity

    private val shoppingCartAdapter by lazy {
        ShoppingCartAdapter()
    }

    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val layoutPosition = viewHolder.layoutPosition
                baseViewModel.updateShoppingCartList(layoutPosition)
                shoppingCartAdapter.notifyChanges()
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseViewModel = ViewModelProvider(requireActivity())[BaseViewModel::class.java]

        binding.recyclerViewShoppingCart.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewShoppingCart.adapter = shoppingCartAdapter

        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.recyclerViewShoppingCart)

        saleActivity = (activity as SaleActivity)
        saleActivity.setEnabledShoppingCartIcon(false)
        saleActivity.changeSaleActivityTopBarTitle("Sepet")

        shoppingCartAdapter.setOnRemoveClickListener {position, _ ->
            baseViewModel.updateShoppingCartList(position)
            shoppingCartAdapter.notifyChanges()
        }

        binding.buttonCreditCard.setOnClickListener {
            baseViewModel.saveToDatabase(true, requireContext(), Constants.CUSTOMER_VKN_TCKN)
        }
        binding.buttonCash.setOnClickListener {
            baseViewModel.saveToDatabase(false, requireContext(), Constants.CUSTOMER_VKN_TCKN)
        }
        binding.buttonEmptyShoppingCart.setOnClickListener {
            showConfirmationDialog()
        }

        observe()
    }

    private fun observe() {
        baseViewModel.statusShoppingCartList.observe(viewLifecycleOwner) {
            shoppingCartAdapter.shoppingCartList = it
            if (it.isEmpty()) {
                moveToBackCategoriesFragment()
            }
        }
        baseViewModel.statusSaveToDatabase.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val intent = Intent(requireActivity(), PaymentActivity::class.java)
                    startActivity(intent)
                    baseViewModel.resetSaveToDatabaseLiveData()
                }
                is Resource.Loading -> {
                    // popup loading screen while waiting move to PaymentActivity
                }
                is Resource.Error -> {
                    toast(requireContext(), it.message ?: "statusSaveToDatabase error!", false)
                }
            }
        }
        baseViewModel.statusShoppingCartQuantity.observe(viewLifecycleOwner) {
            saleActivity.setShoppingCart(it.toString())
        }
        baseViewModel.statusTotal.observe(viewLifecycleOwner) {
            binding.textViewSumCost.text = it
            saleActivity.setShoppingCartTotal(it)
        }
        baseViewModel.statusTotalTax.observe(viewLifecycleOwner) {
            binding.textViewKdvCost.text = it
        }
    }

    private fun showConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(requireActivity().getString(R.string.dialog_empty))
        alertDialogBuilder.setMessage(requireActivity().getString(R.string.dialog_content_empty))

        alertDialogBuilder.setPositiveButton("Evet") { _, _ ->
            baseViewModel.resetShoppingCartList()
            requireActivity().showProgressDialog(Constants.EMPTY_LIST)
            moveToBackCategoriesFragment()
        }
        alertDialogBuilder.setNegativeButton("Hayır") { _, _ ->
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun moveToBackCategoriesFragment() {
        val ftransaction = requireActivity().supportFragmentManager
        ftransaction.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
