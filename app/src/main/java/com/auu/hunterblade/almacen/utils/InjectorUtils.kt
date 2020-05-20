package com.auu.hunterblade.almacen.utils

import android.content.Context
import com.auu.hunterblade.almacen.data.AppDatabase
import com.auu.hunterblade.almacen.data.ProductRepository
import com.auu.hunterblade.almacen.data.SellRepository
import com.auu.hunterblade.almacen.ui.fragments.products.ProductDetailViewModelFactory
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModelFactory
import com.auu.hunterblade.almacen.ui.fragments.sells.SellListViewModelFactory
import com.auu.hunterblade.almacen.ui.fragments.sells.SellViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getProductRepository(context: Context): ProductRepository {
        return ProductRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).ProductDao())
    }

    private fun getSellRepository(context: Context): SellRepository {
        return SellRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).SellDao(),
            AppDatabase.getInstance(context.applicationContext).ProductSellDao()
        )
    }


    fun provideProductViewModelFactory(
        context: Context
    ): ProductViewModelFactory {
        return ProductViewModelFactory(getProductRepository(context))
    }


    fun provideProductDetailViewModelFactory(
        context: Context,
        id: Long
    ): ProductDetailViewModelFactory {
        return ProductDetailViewModelFactory(getProductRepository(context), id)
    }

    fun provideSellListViewModelFactory(
        context: Context
    ): SellListViewModelFactory {
        return SellListViewModelFactory(getSellRepository(context))
    }

    fun provideSellViewModelFactory(
        context: Context,
        id: Long
    ): SellViewModelFactory {
        return SellViewModelFactory(getSellRepository(context), id)
    }

}
