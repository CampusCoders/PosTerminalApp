package com.campuscoders.posterminalapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "OrdersProducts")
data class OrdersProducts(
     @ColumnInfo(name = "order_products_product_id") var orderProductsProductId: String? = null,  //FK
     @ColumnInfo(name = "order_products_order_id") var orderProductsOrderId: String? = null,
     @ColumnInfo(name = "order_products_quantity") var orderProductsQuantity: String? = null,
     @ColumnInfo(name = "order_products_price") var orderProductsPrice: String? = null,
     @ColumnInfo(name = "order_products_price_cents") var orderProductsPriceCents: String? = null,
     @ColumnInfo(name = "order_products_kdv_price") var orderProductsKdvPrice: String? = null,
    @ColumnInfo(name = "order_products_kdv_price_cents") var orderProductsKdvPriceCents: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var orderProductId: Int = 0
}