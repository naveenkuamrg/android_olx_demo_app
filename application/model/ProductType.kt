package com.application.model

import java.util.Locale

enum class ProductType {
    VEHICLES,
    MOBILES,
    ELECTRONICS_AND_APPLIANCES,
    FURNITURE,
    FASHION,
    BOOKS_SPORTS_AND_HOBBIES,
    SPORTS;

    companion object {
        fun stringToProductType(type: String): ProductType? {
            return try {
                ProductType.valueOf(type.trim().uppercase(Locale.ROOT))
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}

