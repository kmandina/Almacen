package com.auu.hunterblade.almacen.ui.fragments.sells

import androidx.lifecycle.ViewModel
import com.auu.hunterblade.almacen.data.ProductRepository
import com.auu.hunterblade.almacen.data.SellRepository

class SellViewModel internal constructor(
    private val sellRepository: SellRepository,
    arg: Long
) : ViewModel() {

    val sell = sellRepository.getProductSellByIdSell(arg)

}