package com.auu.hunterblade.almacen.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SellRepository private constructor(private val sellDao: SellDao, private val productSellDao: ProductSellDao, private val productDao: ProductDao) {

    fun getAllSell() = sellDao.getAllSell()

    fun getSellProducts() = sellDao.getSellProducts()

    fun getSell(id: Long) = sellDao.getSell(id)

    fun getSellProductById(id: Long) = sellDao.getSellProductsById(id)

    fun addSell(sell: Sell) {

        CoroutineScope(Dispatchers.IO).launch {

            sellDao.insert(sell)

        }

    }

    fun addSells(list: List<Sell>) {

        CoroutineScope(Dispatchers.IO).launch {

            sellDao.insertListSell(list)
        }

    }

    fun updateSellNote(id: Long, note: String) {

        CoroutineScope(Dispatchers.IO).launch {

            sellDao.updateSellNoteById(id, note)
        }

    }

    fun deleteSell(sell: Sell) {

        CoroutineScope(Dispatchers.IO).launch {

            sellDao.deleteSell(sell)

        }

    }
    fun deleteAllSell() {

        CoroutineScope(Dispatchers.IO).launch {

            sellDao.deleteAll()

        }

    }

    fun getSumEarn() = sellDao.getSumEarn()

    /* Products Sell */

    fun getAllProductSell() = productSellDao.getAllProductSell()

    fun getProductSell(id: Long) = productSellDao.getProductSell(id)

    fun getProductSellByIdSell(id: Long) = productSellDao.getProductSellByIdSell(id)

    fun addProductSell(productSell: ProductSell, sell: Sell) {

        CoroutineScope(Dispatchers.IO).launch {

            productSellDao.insert(productSell)
            sellDao.updateSellEarnById(sell.idSell, sell.totalEarn + productSell.earnSell)
        }

    }

    fun addProductsSell(list: List<ProductSell>) {

        CoroutineScope(Dispatchers.IO).launch {

            productSellDao.insertListProductSell(list)
        }

    }
    fun deleteProductSell(productSell: ProductSell, sell: Sell) {

        CoroutineScope(Dispatchers.IO).launch {

            productSellDao.deleteProductSell(productSell)
            sellDao.updateSellEarnById(sell.idSell, sell.totalEarn - productSell.earnSell)

        }

    }
    fun deleteAllProductSell() {

        CoroutineScope(Dispatchers.IO).launch {

            productSellDao.deleteAll()

        }

    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: SellRepository? = null

        fun getInstance(sellDao: SellDao, productSellDao: ProductSellDao, productDao: ProductDao) =
            instance ?: synchronized(this) {
                instance ?: SellRepository(sellDao, productSellDao, productDao).also { instance = it }
            }
    }
}