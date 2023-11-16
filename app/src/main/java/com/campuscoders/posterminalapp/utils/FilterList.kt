package com.campuscoders.posterminalapp.utils

import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.model.Products

class FilterList {

    companion object {
        fun <T> bySearch(oldList: List<T>, searchValue: String): List<T> {
            return oldList.filterIndexed { _, t ->
                when (t) {
                    is Products -> {
                        t.productName!!.startsWith(searchValue,true)
                    }

                    is Categories -> {
                        t.categoryName!!.startsWith(searchValue,true)
                    }

                    else -> {
                        false
                    }
                }
            }
        }
    }
}