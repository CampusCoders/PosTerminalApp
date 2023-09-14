package com.campuscoders.posterminalapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Customers")
data class Customers(
    @ColumnInfo(name = "customer_vkn_tckn") var customerVknTckn: String? = "",
    @ColumnInfo(name = "customer_company_name") var customerCompanyName: String? = "",
    @ColumnInfo(name = "customer_first_name") var customerFirstName: String? = "",
    @ColumnInfo(name = "customer_last_name") var customerLastName: String? = "",
    @ColumnInfo(name = "customer_phone_number") var customerPhoneNumber: String? = "",
    @ColumnInfo(name = "customer_email") var customerEmail: String? = "",
    @ColumnInfo(name = "customer_province") var customerProvince: String? = "",
    @ColumnInfo(name = "customer_district") var customerDistrict: String? = "",
    @ColumnInfo(name = "customer_tax_office") var customerTaxOffice: String? = "",
    @ColumnInfo(name = "customer_address") var customerAddress: String? = "",
) {
    @PrimaryKey(autoGenerate = true)
    var customerId: Int = 0
}
