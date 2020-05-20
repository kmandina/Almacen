package com.auu.hunterblade.almacen.ui.fragments.sells

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.auu.hunterblade.almacen.data.ProductRepository
import com.auu.hunterblade.almacen.data.SellRepository
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel

class SellListViewModelFactory (
    private val repos: SellRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SellListViewModel(repos) as T
    }
}