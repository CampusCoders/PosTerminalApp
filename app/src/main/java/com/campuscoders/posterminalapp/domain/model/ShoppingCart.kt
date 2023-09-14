package com.campuscoders.posterminalapp.domain.model

data class ShoppingCart(
    var productId: String,
    var productBarcode: String,
    var productName: String,
    var productQuantity: String,
    var productPrice: String,
    var productPriceCent: String,
    var productKdvPrice: String,
    var productKdvCent: String,
    var productKdv: String,
    var productImage: String
)