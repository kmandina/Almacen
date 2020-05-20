package com.auu.hunterblade.almacen.ui.fragments.products

import androidx.lifecycle.ViewModel
import com.auu.hunterblade.almacen.data.Product
import com.auu.hunterblade.almacen.data.ProductRepository

class ProductViewModel internal constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val lista = productRepository.getAllProduct()

    fun addProduct(prod: Product) = productRepository.addProduct(prod)

}