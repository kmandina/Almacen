package com.auu.hunterblade.almacen.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SellRepository private constructor(private val sellDao: SellDao, private val productSellDao: ProductSellDao) {

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

    /* Products Sell */

    fun getAllProductSell() = productSellDao.getAllProductSell()

    fun getProductSell(id: Long) = productSellDao.getProductSell(id)

    fun getProductSellByIdSell(id: Long) = productSellDao.getProductSellByIdSell(id)

    fun addProductSell(productSell: ProductSell) {

        CoroutineScope(Dispatchers.IO).launch {

            productSellDao.insert(productSell)

        }

    }

    fun addProductsSell(list: List<ProductSell>) {

        CoroutineScope(Dispatchers.IO).launch {

            productSellDao.insertListProductSell(list)
        }

    }
    fun deleteProductSell(productSell: ProductSell) {

        CoroutineScope(Dispatchers.IO).launch {

            productSellDao.deleteProductSell(productSell)

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

        fun getInstance(sellDao: SellDao, productSellDao: ProductSellDao) =
            instance ?: synchronized(this) {
                instance ?: SellRepository(sellDao, productSellDao).also { instance = it }
            }
    }
}