package com.application.model

import java.util.Locale

enum class ProductType {
    VEHICLES,
    MOBILES,
    APPLIANCES,
    FURNITURE,
    FASHION,
    BOOKS,
    SPORTS;

    companion object {
        fun stringToProductType(type: String): ProductType? {
            return try {
                ProductType.valueOf(type.trim().uppercase(Locale.ROOT))
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        @JvmStatic
        fun productTypeToString(type: ProductType?): String{
             return when(type){
                  VEHICLES -> { "Vehicles" }
                  MOBILES -> {"Mobiles"}
                  APPLIANCES -> {"Appliances"}
                  FURNITURE -> {"Furniture"}
                  FASHION -> {"Fashion"}
                  BOOKS -> {"Books"}
                  SPORTS -> {"Sports"}
                 null -> {""}
             }
        }
    }
}

