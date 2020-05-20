package com.auu.hunterblade.almacen.ui.fragments.sells

import androidx.lifecycle.ViewModel
import com.auu.hunterblade.almacen.data.ProductRepository
import com.auu.hunterblade.almacen.data.ProductSell
import com.auu.hunterblade.almacen.data.Sell
import com.auu.hunterblade.almacen.data.SellRepository

class SellViewModel internal constructor(
    private val sellRepository: SellRepository,
    arg: Long
) : ViewModel() {

    val sellList = sellRepository.getProductSellByIdSell(arg)

    val sell = sellRepository.getSell(arg)

    fun addProductSell(product: ProductSell, s: Sell) =  sellRepository.addProductSell(product, s)

}