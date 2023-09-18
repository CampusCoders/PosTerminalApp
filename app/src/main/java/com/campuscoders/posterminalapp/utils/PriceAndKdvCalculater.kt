package com.campuscoders.posterminalapp.utils

class PriceAndKdvCalculater() {
    companion object {
        fun calculateTotalPriceAndKdv(price: Int, priceCent: Int, kdv: Int, quantity: Int): HashMap<String,String> {
            var totalKdvBefore: Int = (price * kdv) / 100 * quantity
            var totalKdvAfter: Int = (priceCent * kdv) / 100 * quantity
            var totalPrice: Int = price * quantity
            var totalPriceCent: Int = priceCent * quantity

            totalKdvBefore += totalKdvAfter / 100
            totalKdvAfter %= 100
            totalPriceCent += totalKdvAfter
            totalPrice += totalKdvBefore + (totalPriceCent / 100)
            totalPriceCent %= 100

            val hashmap = hashMapOf<String,String>()
            hashmap["total_price"] = totalPrice.toString()
            hashmap["total_cent"] = totalPriceCent.toString()
            hashmap["total_kdv_before"] = totalKdvBefore.toString()
            hashmap["total_kdv_after"] = totalKdvAfter.toString()
            return hashmap
        }
    }
}