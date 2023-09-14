package com.campuscoders.posterminalapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Orders")
data class Orders(
    @ColumnInfo(name = "order_customer_id") var orderCustomerId: String? = "",  //FK
    @ColumnInfo(name = "order_receipt_type") var orderReceiptType: String? = "",
    @ColumnInfo(name = "order_payment_type") var orderPaymentType: String? = "",
    @ColumnInfo(name = "order_date") var orderDate: String? = "",
    @ColumnInfo(name = "order_time") var orderTime: String? = "",
    @ColumnInfo(name = "order_status") var orderStatus: String? = "",
    @ColumnInfo(name = "order_receipt_no") var orderReceiptNo: String? = "",
    @ColumnInfo(name = "order_mali_id") var orderMaliId: String? = "",
    @ColumnInfo(name = "order_terminal_id") var orderTerminalId: String? = "",
    @ColumnInfo(name = "order_uye_isyeri_no") var orderUyeIsyeriNo: String? = "",
    @ColumnInfo(name = "order_ettn") var orderETTN: String? = "",
    @ColumnInfo(name = "order_order_no_backend") var orderOrderNoBackend: String? = ""
) {
    @PrimaryKey(autoGenerate = true)
    var orderId: Int = 0
}