package com.auu.hunterblade.almacen.ui.fragments.sells

import androidx.lifecycle.ViewModel
import com.auu.hunterblade.almacen.data.Product
import com.auu.hunterblade.almacen.data.ProductRepository
import com.auu.hunterblade.almacen.data.Sell
import com.auu.hunterblade.almacen.data.SellRepository

class SellListViewModel internal constructor(
    private val sellRepository: SellRepository
) : ViewModel() {

    val lista = sellRepository.getAllSell()

    val sum = sellRepository.getSumEarn()

    fun addSell(sell: Sell) = sellRepository.addSell(sell)

    fun deleteSell(sell: Sell) = sellRepository.deleteSell(sell)

}