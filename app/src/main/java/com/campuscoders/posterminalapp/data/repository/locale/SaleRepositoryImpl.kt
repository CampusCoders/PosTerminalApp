package com.campuscoders.posterminalapp.data.repository.locale

import com.campuscoders.posterminalapp.data.locale.CategoriesDao
import com.campuscoders.posterminalapp.data.locale.CustomersDao
import com.campuscoders.posterminalapp.data.locale.OrdersDao
import com.campuscoders.posterminalapp.data.locale.OrdersProductsDao
import com.campuscoders.posterminalapp.data.locale.ProductsDao
import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.model.Customers
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.model.OrdersProducts
import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    private val categoriesDao: CategoriesDao,
    private val productsDao: ProductsDao,
    private val ordersDao: OrdersDao,
    private val customersDao: CustomersDao,
    private val ordersProductsDao: OrdersProductsDao
): SaleRepository {

    override suspend fun saveAllCategoriesToDatabase(vararg categories: Categories): List<Long> {
        return categoriesDao.insertAllCategories(*categories)
    }

    override suspend fun saveCategoryToDatabase(category: Categories): Long {
        return categoriesDao.insertCategory(category)
    }

    override suspend fun fetchAllCategories(): List<Categories>? {
        return categoriesDao.getCategories()
    }

    override suspend fun saveAllProductsToDatabase(vararg products: Products): List<Long> {
        return productsDao.insertAllProducts(*products)
    }

    override suspend fun saveProductToDatabase(product: Products): Long {
        return productsDao.insertProduct(product)
    }

    override suspend fun fetchAllProducts(): List<Products>? {
        return productsDao.getProducts()
    }

    override suspend fun fetchAllProductsByCategoryId(categoryId: String): List<Products>? {
        return productsDao.getProductsByCategoryId(categoryId)
    }

    override suspend fun fetchProductByProductId(productId: String): Products? {
        return productsDao.getProductByProductId(productId)
    }

    override suspend fun fetchProductByBarcode(barcode: String): Products? {
        return productsDao.queryProductByBarcode(barcode)
    }

    override suspend fun saveOrderToDatabase(order: Orders): Long {
        return ordersDao.insertOrderToDatabase(order)
    }

    override suspend fun fetchAllOrders(): List<Orders>? {
        return ordersDao.getOrders()
    }

    override suspend fun fetchOrderByCustomerId(customerId: String): Orders? {
        return ordersDao.getOrderByCustomerId(customerId)
    }

    override suspend fun fetchOrderById(orderId: String): Orders? {
        return ordersDao.getOrderById(orderId)
    }

    override suspend fun updateOrder(orderReceiptType: String, orderDate: String, orderTime: String, orderStatus: String, orderReceiptNo: String, orderId: String, orderTotal: String, orderTotalTax: String): Int {
        return ordersDao.updateOrder(orderReceiptType, orderDate, orderTime, orderStatus, orderReceiptNo, orderId, orderTotal, orderTotalTax)
    }

    override suspend fun saveCustomerToDatabase(customer: Customers): Long {
        return customersDao.insertCustomer(customer)
    }

    override suspend fun fetchCustomerByVknTckn(customerVknTckn: String): Customers? {
        return customersDao.queryCustomerByVknTckn(customerVknTckn)
    }

    override suspend fun fetchAllCustomers(): List<Customers>? {
        return customersDao.queryAllCustomers()
    }

    override suspend fun updateCustomer(customer: Customers): Int {
        return customersDao.updateCustomer(customer)
    }

    override suspend fun saveAllOrdersProductsToDatabase(vararg ordersProducts: OrdersProducts): List<Long> {
        return ordersProductsDao.insertAllOrdersProducts(*ordersProducts)
    }

    override suspend fun fetchAllOrdersProducts(): List<OrdersProducts>? {
        return ordersProductsDao.getOrdersProducts()
    }
}