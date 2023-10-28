package com.campuscoders.posterminalapp.presentation.sale.views

import com.campuscoders.posterminalapp.domain.model.ShoppingCart

class ShoppingCartItems {

    companion object {
        private var shoppingCartList = arrayListOf<ShoppingCart>()
        private var productTotalPrice = ""
        private var productTotalKdv = ""
        private var customerName = ""
        private var orderId = ""
        private var orderNo = ""
        private var paymentType = ""
        private var date = ""
        private var time = ""

        fun setShoppingCartList(newList: ArrayList<ShoppingCart>) { shoppingCartList = newList }
        fun getShoppingCartList() = shoppingCartList

        fun setTotalPrice(totalPrice: String) { productTotalPrice = totalPrice }
        fun getTotalPrice() = productTotalPrice

        fun setTotalTax(totalKdv: String) { productTotalKdv = totalKdv }
        fun getTotalTax() = productTotalKdv

        fun setCustomerName(customerName: String) { this.customerName = customerName }
        fun getCustomerName() = customerName

        fun setOrderId(orderId: String) { this.orderId = orderId }
        fun getOrderId() = orderId

        fun setOrderNo(orderNo: String) { this.orderNo = orderNo }
        fun getOrderNo() = orderNo

        fun setPaymentType(paymentType: String) { this.paymentType = paymentType }
        fun getPaymentType() = paymentType

        fun setDate(date: String) { this.date = date }
        fun getDate() = date

        fun setTime(time: String) { this.time = time }
        fun getTime() = time
    }

}