package com.campuscoders.posterminalapp.presentation.sale

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.model.OrdersProducts
import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.model.ShoppingCart
import com.campuscoders.posterminalapp.domain.use_case.sale.FetchAllCategoriesUseCase
import com.campuscoders.posterminalapp.domain.use_case.sale.FetchAllProductsByCategoryIdUseCase
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
class BaseViewModel @Inject constructor(
    private val fetchProductByProductIdUseCase: FetchProductByProductIdUseCase,
    private val fetchAllProductsByCategoryIdUseCase: FetchAllProductsByCategoryIdUseCase,
    private val saveOrderUseCase: SaveOrderUseCase,
    private val saveAllOrdersProductsByUseCase: SaveAllOrdersProductsUseCase,
    private val fetchCustomerByVknTcknUseCase: FetchCustomerByVknTcknUseCase,
    private val fetchAllCategoriesUseCase: FetchAllCategoriesUseCase
) : ViewModel() {

    private var _statusCategoriesList = MutableLiveData<Resource<List<Categories>>>()
    val statusCategoriesList: LiveData<Resource<List<Categories>>>
        get() = _statusCategoriesList

    private var _statusTotal = MutableLiveData<String>()
    val statusTotal: LiveData<String>
        get() = _statusTotal

    private var _statusTotalTax = MutableLiveData<String>()
    val statusTotalTax: LiveData<String>
        get() = _statusTotalTax

    private var _statusShoppingCartList = MutableLiveData<MutableList<ShoppingCart>>()
    val statusShoppingCartList: LiveData<MutableList<ShoppingCart>>
        get() = _statusShoppingCartList

    private var _statusShoppingCartQuantity = MutableLiveData<Int>()
    val statusShoppingCartQuantity: LiveData<Int>
        get() = _statusShoppingCartQuantity

    private var _statusProductsList = MutableLiveData<Resource<List<Products>>>()
    val statusProductsList: LiveData<Resource<List<Products>>>
        get() = _statusProductsList

    private var _statusSaveToDatabase = MutableLiveData<Resource<Boolean>>()
    val statusSaveToDatabase: LiveData<Resource<Boolean>>
        get() = _statusSaveToDatabase

    fun getCategories() {
        _statusCategoriesList.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = fetchAllCategoriesUseCase.executeFetchAllCategories()
            _statusCategoriesList.value = response
        }
    }

    fun getProductsByCategoryId(categoryId: String) {
        _statusProductsList.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = fetchAllProductsByCategoryIdUseCase.executeFetchAllProductsByCategoryId(categoryId)
            _statusProductsList.value = response
        }
    }

    private fun calculateTotal() {
        _statusShoppingCartList.value?.let { shoppingCartList ->
            var totalPrice = 0
            var totalPriceCent = 0
            var totalKdv = 0
            var totalKdvCent = 0
            for (item in shoppingCartList) {
                totalPrice += item.productPrice.toInt()
                totalPriceCent += item.productPriceCent.toInt()
                totalKdv += item.productKdvPrice.toInt()
                totalKdvCent += item.productKdvCent.toInt()
            }
            totalPrice += totalPriceCent / 100
            totalPriceCent %= 100
            totalKdv += totalKdvCent / 100
            totalKdvCent %= 100

            ShoppingCartItems.setTotalPrice("$totalPrice,${totalPriceCent.toCent()}")
            ShoppingCartItems.setTotalTax("$totalKdv,${totalKdvCent.toCent()}")

            _statusTotal.value = "₺$totalPrice,${totalPriceCent.toCent()}"
            _statusTotalTax.value = "₺$totalKdv,${totalKdvCent.toCent()}"
        }
        _statusShoppingCartQuantity.value = _statusShoppingCartList.value?.size
    }

    fun addProduct(productId: String) {

        var isProductExists = false

        if (_statusShoppingCartList.value == null) {
            _statusShoppingCartList.value = mutableListOf()
        }

        _statusShoppingCartList.value?.let { shoppingCartList ->
            for (item in shoppingCartList) {
                if (productId == item.productId) {
                    item.productQuantity = (item.productQuantity.toInt() + 1).toString()

                    val hashmap = PriceAndKdvCalculator.calculateTotalPriceAndKdv(item.originalPrice.toInt(),item.originalPriceCent.toInt(),
                        item.productKdv.toInt(),item.productQuantity.toInt())
                    hashmap["total_cent"] = hashmap["total_cent"]!!.toInt().toCent()
                    hashmap["total_kdv_after"] = hashmap["total_kdv_after"]!!.toInt().toCent()

                    item.productPrice = hashmap["total_price"]?:""
                    item.productPriceCent = hashmap["total_cent"]?:""
                    item.productKdvPrice = hashmap["total_kdv_before"]?:""
                    item.productKdvCent = hashmap["total_kdv_after"]?:""

                    isProductExists = true
                    calculateTotal()
                }
            }
        }

        if (!isProductExists) {
            val newShoppingCart = _statusShoppingCartList.value
            viewModelScope.launch {
                val response = fetchProductByProductIdUseCase.executeFetchProductByProductId(productId)
                if (response is Resource.Success) {
                    response.data?.let {
                        val hashmap = PriceAndKdvCalculator.calculateTotalPriceAndKdv(it.productPrice!!.toInt(),it.productPriceCents!!.toInt(),
                            it.productKdv!!.toInt(),1)
                        hashmap["total_cent"] = hashmap["total_cent"]!!.toInt().toCent()
                        hashmap["total_kdv_after"] = hashmap["total_kdv_after"]!!.toInt().toCent()

                        newShoppingCart?.add(ShoppingCart(it.productId.toString(),it.productBarcode!!,it.productName!!,"1", hashmap["total_price"]?:"",
                                hashmap["total_cent"]?:"",hashmap["total_kdv_before"]?:"",hashmap["total_kdv_after"]?:"",
                                it.productKdv!!,it.productImage!!,it.productPrice.toString(),it.productPriceCents.toString()))

                        ShoppingCartItems.setShoppingCartList(arrayListOf())
                        _statusShoppingCartList.value = newShoppingCart?: mutableListOf()
                        calculateTotal()
                    }
                } else if (response is Resource.Error) {
                    println("response is Resource.Error")
                }
            }
        }
    }

    fun decreaseProduct(productId: String, position: Int) {
        _statusShoppingCartList.value?.let { shoppingCartList ->
            for (item in shoppingCartList) {
                if (productId == item.productId) {
                    item.productQuantity = (item.productQuantity.toInt() - 1).toString()

                    if (item.productQuantity == "0") {
                        updateShoppingCartList(productId)
                    } else {
                        val hashmap = PriceAndKdvCalculator.calculateTotalPriceAndKdv(item.originalPrice.toInt(),item.originalPriceCent.toInt(),
                            item.productKdv.toInt(),item.productQuantity.toInt())
                        hashmap["total_cent"] = hashmap["total_cent"]!!.toInt().toCent()
                        hashmap["total_kdv_after"] = hashmap["total_kdv_after"]!!.toInt().toCent()

                        item.productPrice = hashmap["total_price"]?:""
                        item.productPriceCent = hashmap["total_cent"]?:""
                        item.productKdvPrice = hashmap["total_kdv_before"]?:""
                        item.productKdvCent = hashmap["total_kdv_after"]?:""
                    }

                    calculateTotal()
                }
            }
        }
    }

    fun saveToDatabase(isCreditCard: Boolean, context: Context, vknTckn: String) {
        _statusSaveToDatabase.value = Resource.Loading(null)

        var paymentType = "Nakit"
        if (isCreditCard) paymentType = "Kredi Kartı"

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
        val mainUserInfo = customSharedPreferences.getMainUserLogin()

        viewModelScope.launch {
            val orderId = saveOrderUseCase.executeSaveOrder(
                Orders(customerId,empty, paymentType, empty, empty, empty, empty, Constants.ORDER_MALI_ID,mainUserInfo["terminal_id"].toString(),
                    mainUserInfo["uye_isyeri_no"].toString(), Constants.ORDER_ETTN, Constants.ORDER_NO_BACKEND, empty, empty)
            )
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
            _statusShoppingCartList.value?.let {
                for (shoppingCartItem in it) {
                    ordersProductsList.add(
                        OrdersProducts(shoppingCartItem.productId,orderId,shoppingCartItem.productQuantity,
                        shoppingCartItem.productPrice,shoppingCartItem.productPriceCent,shoppingCartItem.productKdvPrice,
                        shoppingCartItem.productKdvCent)
                    )
                }
            }
            val newList = ordersProductsList.toTypedArray()
            val response = saveAllOrdersProductsByUseCase.executeSaveAllOrdersProducts(*newList)

            if (response is Resource.Success) _statusSaveToDatabase.value = Resource.Success(true)
            else _statusSaveToDatabase.value = Resource.Error(null,response.message?:"error saveOrdersProducts")
        }
    }

    fun updateShoppingCartList(productId: String) {
        _statusShoppingCartList.value = _statusShoppingCartList.value?.let {
            it.filterIndexed { _, shoppingCart ->
                shoppingCart.productId != productId
            }
        }?.toMutableList()
        calculateTotal()
    }

    fun resetSaveToDatabaseLiveData() {
        _statusSaveToDatabase = MutableLiveData<Resource<Boolean>>()
    }

    fun resetShoppingCartList() {
        _statusShoppingCartList.value = mutableListOf()
        calculateTotal()
    }
}