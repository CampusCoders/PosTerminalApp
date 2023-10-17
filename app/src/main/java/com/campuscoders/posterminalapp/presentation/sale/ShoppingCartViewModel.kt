package com.campuscoders.posterminalapp.presentation.sale

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.model.OrdersProducts
import com.campuscoders.posterminalapp.domain.model.ShoppingCart
import com.campuscoders.posterminalapp.domain.use_case.sale.FetchCustomerByVknTcknUseCase
import com.campuscoders.posterminalapp.domain.use_case.sale.FetchProductByProductIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.sale.SaveAllOrdersProductsUseCase
import com.campuscoders.posterminalapp.domain.use_case.sale.SaveOrderUseCase
import com.campuscoders.posterminalapp.presentation.sale.views.ShoppingCartItems
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.CustomSharedPreferences
import com.campuscoders.posterminalapp.utils.PriceAndKdvCalculator
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.toCent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val fetchProductByProductIdUseCase: FetchProductByProductIdUseCase,
    private val saveOrderUseCase: SaveOrderUseCase,
    private val saveAllOrdersProductsByUseCase: SaveAllOrdersProductsUseCase,
    private val fetchCustomerByVknTcknUseCase: FetchCustomerByVknTcknUseCase
): ViewModel() {

    private var _statusShoppingCartList = MutableLiveData<Resource<List<ShoppingCart>>>()
    val statusShoppingCartList: LiveData<Resource<List<ShoppingCart>>>
        get() = _statusShoppingCartList

    private var _statusSaveToDatabase = MutableLiveData<Resource<Boolean>>()
    val statusSaveToDatabase: LiveData<Resource<Boolean>>
        get() = _statusSaveToDatabase

    fun showShoppingCartList(hashmap: HashMap<String,Int>) {
        val shoppingCartArray = arrayListOf<ShoppingCart>()
        viewModelScope.launch {
            for ((productId,quantity) in hashmap) {
                val product = fetchProductByProductIdUseCase.executeFetchProductByProductId(productId)
                when(product) {
                    is Resource.Success -> {
                        product.data?.let {
                            val hashmap = PriceAndKdvCalculator.calculateTotalPriceAndKdv(it.productPrice!!.toInt(),it.productPriceCents!!.toInt(),it.productKdv!!.toInt(),quantity)
                            hashmap["total_cent"] = hashmap["total_cent"]!!.toInt().toCent()
                            hashmap["total_kdv_after"] = hashmap["total_kdv_after"]!!.toInt().toCent()
                            shoppingCartArray.add(
                                ShoppingCart(it.productId.toString(),it.productBarcode!!,it.productName!!,quantity.toString(), hashmap["total_price"]?:"",
                                    hashmap["total_cent"]?:"",hashmap["total_kdv_before"]?:"",hashmap["total_kdv_after"]?:"",it.productKdv!!,it.productImage!!)
                            )
                        }
                    }
                    is Resource.Loading -> { _statusShoppingCartList.value = Resource.Loading(null) }
                    is Resource.Error -> { _statusShoppingCartList.value = Resource.Error(null,"error") }
                }
            }
            ShoppingCartItems.setShoppingCartList(shoppingCartArray)
            _statusShoppingCartList.value = Resource.Success(shoppingCartArray)
        }

    }

    fun updateShoppingCartList(position: Int) {
        _statusShoppingCartList.value = _statusShoppingCartList.value?.data?.let {
            Resource.Success(it.filterIndexed { index, _ ->
                index != position
            })
        }
    }

    fun saveToDatabase(isCreditCard: Boolean, context: Context, vknTckn: String) {
        _statusSaveToDatabase.value = Resource.Loading(null)

        var paymentType = "Cash"
        if (isCreditCard) paymentType = "CreditCard"

        viewModelScope.launch {
            val customerData = fetchCustomerByVknTcknUseCase.executeFetchCustomerByVknTckn(vknTckn)
            if (customerData is Resource.Success) {
                ShoppingCartItems.setCustomerName("${customerData.data!!.customerFirstName} ${customerData.data.customerLastName}")
                ShoppingCartItems.setPaymentType(paymentType)
                saveOrder(paymentType,context,customerData.data.customerId.toString())
            } else {
                _statusSaveToDatabase.value = Resource.Error(false,customerData.message?:"Error saveToDatabase")
            }
        }
    }

    private fun saveOrder(paymentType: String, context: Context, customerId: String) {
        _statusSaveToDatabase.value = Resource.Loading(null)

        val empty = ""
        val customSharedPreferences = CustomSharedPreferences(context)
        val mainUserInfos = customSharedPreferences.getMainUserLogin()

        viewModelScope.launch {
            val orderId = saveOrderUseCase.executeSaveOrder(
                Orders(customerId,empty, paymentType, empty, empty, empty, empty, Constants.ORDER_MALI_ID,mainUserInfos["terminal_id"].toString(),
                    mainUserInfos["uye_isyeri_no"].toString(), Constants.ORDER_ETTN, Constants.ORDER_NO_BACKEND))
            if (orderId is Resource.Success) {
                ShoppingCartItems.setOrderId(orderId.data.toString())
                saveOrdersProducts(orderId.data.toString())
            } else {
                _statusSaveToDatabase.value = Resource.Error(null,orderId.message?:"error saveOrder")
            }
        }
    }

    private fun saveOrdersProducts(orderId: String) {
        _statusSaveToDatabase.value = Resource.Loading(null)
        val ordersProductsList = arrayListOf<OrdersProducts>()

        viewModelScope.launch {
            _statusShoppingCartList.value?.data?.let {
                for (shoppingCartItem in it) {
                    ordersProductsList.add(OrdersProducts(shoppingCartItem.productId,orderId,shoppingCartItem.productQuantity,
                        shoppingCartItem.productPrice,shoppingCartItem.productPriceCent,shoppingCartItem.productKdvPrice,
                        shoppingCartItem.productKdvCent))
                }
            }
            val newList = ordersProductsList.toTypedArray()
            val response = saveAllOrdersProductsByUseCase.executeSaveAllOrdersProducts(*newList)

            if (response is Resource.Success) _statusSaveToDatabase.value = Resource.Success(true)
            else _statusSaveToDatabase.value = Resource.Error(null,response.message?:"error saveOrdersProducts")
        }
    }

    fun resetSaveToDatabaseLiveData() {
        _statusSaveToDatabase = MutableLiveData<Resource<Boolean>>()
    }
}