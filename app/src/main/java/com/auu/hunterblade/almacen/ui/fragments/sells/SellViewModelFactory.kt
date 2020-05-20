package com.auu.hunterblade.almacen.ui.fragments.sells

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.auu.hunterblade.almacen.data.ProductRepository
import com.auu.hunterblade.almacen.data.SellRepository
import com.auu.hunterblade.almacen.ui.fragments.products.ProductDetailViewModel

class SellViewModelFactory (
    private val repos: SellRepository,
    private val id: Long
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SellViewModel(repos, id) as T
    }
}