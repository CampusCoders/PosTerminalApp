package com.campuscoders.posterminalapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Products")
data class Products(
    @ColumnInfo(name = "product_category_id") var productCategoryId: String? = "",  //FK
    @ColumnInfo(name = "product_name") var productName: String? = "",
    @ColumnInfo(name = "product_code") var productCode: String? = "",
    @ColumnInfo(name = "product_description") var productDescription: String? = "",
    @ColumnInfo(name = "product_image") var productImage: String? = "",
    @ColumnInfo(name = "product_barcode") var productBarcode: String? = "",
    @ColumnInfo(name = "product_price") var productPrice: String? = "",
    @ColumnInfo(name = "product_price_cents") var productPriceCents: String? = "",
    @ColumnInfo(name = "product_kdv") var productKdv: String? = "",
    @ColumnInfo(name = "product_stappage") var productStappage: String? = "",
    @ColumnInfo(name = "product_quantity") var productQuantity: String? = "",
    @ColumnInfo(name = "product_discount") var productDiscount: String? = "",
    @ColumnInfo(name = "product_exception_code") var productExceptionCode: String? = "",
    @ColumnInfo(name = "product_exception_description") var productExceptionCodeDescription: String? = ""
) {
    @PrimaryKey(autoGenerate = true)
    var productId: Int = 0
}
