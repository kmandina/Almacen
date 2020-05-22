package com.auu.hunterblade.almacen.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductRepository private constructor(private val productDao: ProductDao) {

    fun getAllProduct() = productDao.getAllProduct()

    fun getProduct(id: Long) = productDao.getProduct(id)

    fun deleteProduct(product: Product){

        CoroutineScope(Dispatchers.IO).launch {

            productDao.deleteProduct(product)

        }

    }

    fun deleteAllProducts() {

        CoroutineScope(Dispatchers.IO).launch {

            productDao.deleteAll()

        }

    }

    fun addListProducts(products: List<Product>) {

        CoroutineScope(Dispatchers.IO).launch {

            productDao.insertListProduct(products)

        }

    }

    fun addProduct(producto: Product) {

        CoroutineScope(Dispatchers.IO).launch {

            productDao.insert(producto)

        }

    }

    fun updateProductById(id: Long, value: Long){

        CoroutineScope(Dispatchers.IO).launch {

            productDao.updateProductById(id, value)

        }
    }

    fun updateProduct(product: Product){

        CoroutineScope(Dispatchers.IO).launch {

            productDao.updateProduct(product)

        }

    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: ProductRepository? = null

        fun getInstance(productDao: ProductDao) =
            instance ?: synchronized(this) {
                instance ?: ProductRepository(productDao).also { instance = it }
            }
    }
}