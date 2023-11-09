package com.campuscoders.posterminalapp.domain.repository.locale

import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.model.Customers
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.model.OrdersProducts
import com.campuscoders.posterminalapp.domain.model.Products

interface SaleRepository {

    suspend fun saveAllCategoriesToDatabase(vararg categories: Categories): List<Long>

    suspend fun saveCategoryToDatabase(category: Categories): Long

    suspend fun fetchAllCategories(): List<Categories>?

    suspend fun saveAllProductsToDatabase(vararg products: Products): List<Long>

    suspend fun saveProductToDatabase(product: Products): Long

    suspend fun fetchAllProducts(): List<Products>?

    suspend fun fetchAllProductsByCategoryId(categoryId: String): List<Products>?

    suspend fun fetchProductByProductId(productId: String): Products?

    suspend fun fetchProductByBarcode(barcode: String): Products?

    suspend fun saveOrderToDatabase(order: Orders): Long

    suspend fun fetchAllOrders(): List<Orders>?

    suspend fun fetchOrderByCustomerId(customerId: String): Orders?

    suspend fun fetchOrderById(orderId: String): Orders?

    suspend fun updateOrder(orderReceiptType: String, orderDate: String, orderTime: String, orderStatus: String, orderReceiptNo: String,
                            orderId: String, orderTotal: String, orderTotalTax: String, timestamp: Long): Int

    suspend fun saveCustomerToDatabase(customer: Customers): Long

    suspend fun fetchCustomerByVknTckn(customerVknTckn: String): Customers?

    suspend fun fetchAllCustomers(): List<Customers>?

    suspend fun updateCustomer(customer: Customers): Int

    suspend fun saveAllOrdersProductsToDatabase(vararg ordersProducts: OrdersProducts): List<Long>

    suspend fun fetchAllOrdersProducts(): List<OrdersProducts>?
}