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

    @Query("DELETE from product WHERE id = :id")
    suspend fun deleteProductById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListProduct(products: List<Product>)

    @Query("UPDATE product SET amount = :value WHERE id = :id")
    fun updateProductById(id: Long, value: Long)

    @Update
    fun updateProduct(product: Product)
}