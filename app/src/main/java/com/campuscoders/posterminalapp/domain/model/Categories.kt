package com.campuscoders.posterminalapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Categories")
data class Categories(
    @ColumnInfo(name = "category_code") var categoryCode: String? = "",
    @ColumnInfo(name = "category_name") var categoryName: String? = "",
    @ColumnInfo(name = "category_description") var categoryDescription: String? = "",
    @ColumnInfo(name = "category_image") var categoryImage: String? = "",
) {
    @PrimaryKey(autoGenerate = true)
    var categoryId: Int = 0
}
