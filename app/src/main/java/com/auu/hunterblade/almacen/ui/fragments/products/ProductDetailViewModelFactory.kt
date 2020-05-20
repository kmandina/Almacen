package com.auu.hunterblade.almacen.ui.fragments.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.auu.hunterblade.almacen.data.ProductRepository

class ProductDetailViewModelFactory (
    private val repos: ProductRepository,
    private val id: Long
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductDetailViewModel(repos, id) as T
    }
}