package com.auu.hunterblade.almacen.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Query("SELECT * from product ORDER BY id ASC")
    fun getAllProduct(): LiveData<List<Product>>

    @Query("SELECT * FROM product WHERE id = :id")
    fun getProduct(id: Long): LiveData<Product>

    @Query("DELETE from product")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListProduct(products: List<Product>)
}