package com.auu.hunterblade.almacen.ui.fragments.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.auu.hunterblade.almacen.data.ProductRepository

class ProductViewModelFactory (
    private val repos: ProductRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(repos) as T
    }
}