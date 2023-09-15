package com.campuscoders.posterminalapp.data.locale

import androidx.room.Database
import androidx.room.RoomDatabase
import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.model.Customers
import com.campuscoders.posterminalapp.domain.model.MainUser
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.model.OrdersProducts
import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.model.TerminalUsers

@Database(
    entities = [MainUser::class, TerminalUsers::class, Products::class, Orders::class, OrdersProducts::class, Customers::class, Categories::class],
    version = 1)
abstract class PosDatabase: RoomDatabase() {
    abstract fun mainUserDao(): MainUserDao
    abstract fun terminalUsersDao(): TerminalUsersDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun productsDao(): ProductsDao
    abstract fun ordersDao(): OrdersDao
    abstract fun customersDao(): CustomersDao
    abstract fun ordersProductsDao(): OrdersProductsDao
}