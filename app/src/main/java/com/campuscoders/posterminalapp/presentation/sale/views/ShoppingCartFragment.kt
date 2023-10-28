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
import com.campuscoders.posterminalapp.domain.model.ShoppingCart
import com.campuscoders.posterminalapp.presentation.PaymentActivity
import com.campuscoders.posterminalapp.presentation.SaleActivity
import com.campuscoders.posterminalapp.presentation.sale.ShoppingCartViewModel
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.toCent
import com.campuscoders.posterminalapp.utils.toast

class ShoppingCartFragment : Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ShoppingCartViewModel

    private lateinit var saleActivity: SaleActivity

    private var hashmap = hashMapOf<String, Int>()

    private val shoppingCartAdapter by lazy {
        ShoppingCartAdapter()
    }

    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val layoutPosition = viewHolder.layoutPosition
                val shoppingCartItem = shoppingCartAdapter.shoppingCartList[layoutPosition]

                viewModel.updateShoppingCartList(layoutPosition)
                shoppingCartAdapter.notifyChanges()

                val saleActivity = (activity as SaleActivity)

                hashmap.remove(shoppingCartItem.productId)
                saleActivity.setShoppingCart(hashmap)
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ShoppingCartViewModel::class.java]

        binding.recyclerViewShoppingCart.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewShoppingCart.adapter = shoppingCartAdapter

        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.recyclerViewShoppingCart)

        saleActivity = (activity as SaleActivity)
        saleActivity.setEnabledShoppingCartIcon(false)
        saleActivity.changeSaleActivityTopBarTitle("Sepet")

        hashmap = saleActivity.getHashmap()
        viewModel.showShoppingCartList(hashmap)

        shoppingCartAdapter.setOnRemoveClickListener {position, productId ->
            viewModel.updateShoppingCartList(position)
            shoppingCartAdapter.notifyChanges()

            hashmap.remove(productId)
            saleActivity.setShoppingCart(hashmap)
        }

        binding.buttonCreditCard.setOnClickListener {
            viewModel.saveToDatabase(true, requireContext(), Constants.CUSTOMER_VKN_TCKN)
        }
        binding.buttonCash.setOnClickListener {
            viewModel.saveToDatabase(false, requireContext(), Constants.CUSTOMER_VKN_TCKN)
        }
        binding.buttonEmptyShoppingCart.setOnClickListener {
            showConfirmationDialog()
        }

        observe()
    }

    private fun observe() {
        viewModel.statusShoppingCartList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (it.data!!.isNotEmpty()) {
                        shoppingCartAdapter.shoppingCartList = it.data
                        showTotalPriceAndKdv(it.data)
                    } else {
                        shoppingCartAdapter.shoppingCartList = it.data
                        moveToBackCategoriesFragment()
                    }
                }
                is Resource.Loading -> {}
                is Resource.Error -> {
                    toast(requireContext(), it.message ?: "shoppingCart list error", false)
                }
            }
        }
        viewModel.statusSaveToDatabase.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val intent = Intent(requireActivity(), PaymentActivity::class.java)
                    startActivity(intent)
                    viewModel.resetSaveToDatabaseLiveData()
                }
                is Resource.Loading -> {
                    // popup loading screen while waiting move to PaymentActivity
                }
                is Resource.Error -> {
                    toast(requireContext(), it.message ?: "statusSaveToDatabase error!", false)
                }
            }
        }
    }

    private fun showConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(requireActivity().getString(R.string.dialog_empty))
        alertDialogBuilder.setMessage(requireActivity().getString(R.string.dialog_content_empty))

        alertDialogBuilder.setPositiveButton("Evet") { _, _ ->
            saleActivity.setShoppingCart(hashMapOf<String, Int>())
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

    private fun showTotalPriceAndKdv(list: List<ShoppingCart>) {
        var totalPrice = 0
        var totalPriceCent = 0
        var totalKdv = 0
        var totalKdvCent = 0
        for (i in list) {
            totalPrice += i.productPrice.toInt()
            totalPriceCent += i.productPriceCent.toInt()
            totalKdv += i.productKdvPrice.toInt()
            totalKdvCent += i.productKdvCent.toInt()
        }
        totalPrice += totalPriceCent / 100
        totalPriceCent %= 100
        totalKdv += totalKdvCent / 100
        totalKdvCent %= 100

        ShoppingCartItems.setTotalPrice("$totalPrice,${totalPriceCent.toCent()}")
        ShoppingCartItems.setTotalTax("$totalKdv,${totalKdvCent.toCent()}")

        binding.textViewSumCost.text = "₺$totalPrice,${totalPriceCent.toCent()}"
        binding.textViewKdvCost.text = "₺$totalKdv,${totalKdvCent.toCent()}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
