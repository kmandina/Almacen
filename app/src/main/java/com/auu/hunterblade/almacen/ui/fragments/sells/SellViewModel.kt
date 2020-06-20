package com.auu.hunterblade.almacen.ui.fragments.sells

import androidx.lifecycle.ViewModel
import com.auu.hunterblade.almacen.data.ProductRepository
import com.auu.hunterblade.almacen.data.ProductSell
import com.auu.hunterblade.almacen.data.Sell
import com.auu.hunterblade.almacen.data.SellRepository

class SellViewModel internal constructor(
    private val sellRepository: SellRepository,
    private val arg: Long
) : ViewModel() {

    val sellList = sellRepository.getProductSellByIdSell(arg)

    val sell = sellRepository.getSell(arg)

    fun addProductSell(product: ProductSell, s: Sell) =  sellRepository.addProductSell(product, s)

//    fun updateSell(note: String) = sellRepository.updateSellNote(arg, note)

    fun updateSell(sell: Sell) = sellRepository.updateSell(sell)

    fun deleteSell(productSell: ProductSell, s: Sell) = sellRepository.deleteProductSell(productSell, s)
}