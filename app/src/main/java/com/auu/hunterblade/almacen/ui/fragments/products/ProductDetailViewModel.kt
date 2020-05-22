package com.auu.hunterblade.almacen.ui.fragments.products

import androidx.lifecycle.ViewModel
import com.auu.hunterblade.almacen.data.Product
import com.auu.hunterblade.almacen.data.ProductRepository

class ProductDetailViewModel internal constructor(
    private val productRepository: ProductRepository,
    arg: Long
) : ViewModel() {

    val product = productRepository.getProduct(arg)

    fun updateProduct(product: Product) = productRepository.updateProduct(product)

}