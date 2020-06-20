package com.auu.hunterblade.almacen.ui.fragments.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.auu.hunterblade.almacen.data.SellRepository

class ProductSellViewModelFactory (
    private val repos: SellRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductSellViewModel(repos) as T
    }
}