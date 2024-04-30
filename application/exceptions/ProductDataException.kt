package com.application.exceptions

sealed class ProductDataException(message: String) : Exception(message) {
    class ProductDataDeleteException : ProductDataException("Product is deleted by seller")
}