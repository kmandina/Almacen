package com.auu.hunterblade.almacen.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import com.auu.hunterblade.almacen.data.AppDatabase
import com.auu.hunterblade.almacen.data.ProductRepository
import com.auu.hunterblade.almacen.data.SellRepository
import com.auu.hunterblade.almacen.ui.fragments.dashboard.ProductSellViewModelFactory
import com.auu.hunterblade.almacen.ui.fragments.products.ProductDetailViewModelFactory
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModelFactory
import com.auu.hunterblade.almacen.ui.fragments.sells.SellListViewModelFactory
import com.auu.hunterblade.almacen.ui.fragments.sells.SellViewModelFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

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
            AppDatabase.getInstance(context.applicationContext).ProductSellDao(),
            AppDatabase.getInstance(context.applicationContext).ProductDao()
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

    fun provideProductSellListViewModelFactory(
        context: Context
    ): ProductSellViewModelFactory {
        return ProductSellViewModelFactory(getSellRepository(context))
    }

    fun provideSellViewModelFactory(
        context: Context,
        id: Long
    ): SellViewModelFactory {
        return SellViewModelFactory(getSellRepository(context), id)
    }

    fun AppFileDir(context: Context): File {

        return if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
        {
            context.getExternalFilesDir(null)!!
        }else {
            context.filesDir
        }
    }

    fun getExternalStorageDirectoryCustom(context: Context): File {

        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            Environment.getExternalStorageDirectory()
        }else {
            AppFileDir(context)
        }
    }

//    fun exportDatabase(){
//        val sd = Environment.getExternalStorageDirectory()
//
//        // Get the Room database storage path using SupportSQLiteOpenHelper
//        AppDatabase.getDatabase(applicationContext)!!.openHelper.writableDatabase.path
//
//        if (sd.canWrite()) {
//            val currentDBPath = AppDatabase.getDatabase(applicationContext)!!.openHelper.writableDatabase.path
//            val backupDBPath = "mydb.sqlite"      //you can modify the file type you need to export
//            val currentDB = File(currentDBPath)
//            val backupDB = File(sd, backupDBPath)
//            if (currentDB.exists()) {
//                try {
//                    val src = FileInputStream(currentDB).channel
//                    val dst = FileOutputStream(backupDB).channel
//                    dst.transferFrom(src, 0, src.size())
//                    src.close()
//                    dst.close()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }

}
