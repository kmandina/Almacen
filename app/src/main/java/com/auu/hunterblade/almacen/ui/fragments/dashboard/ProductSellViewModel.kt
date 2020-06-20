package com.auu.hunterblade.almacen.ui.fragments.dashboard

import androidx.lifecycle.ViewModel
import com.auu.hunterblade.almacen.data.SellRepository

class ProductSellViewModel internal constructor(
    private val sellRepository: SellRepository
) : ViewModel() {

    val lista = sellRepository.getAllProductSell()

}