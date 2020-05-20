package com.auu.hunterblade.almacen.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductSellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productSell: ProductSell)

    @Query("SELECT * from productSell ORDER BY id ASC")
    fun getAllProductSell(): LiveData<List<ProductSell>>

    @Query("SELECT * FROM productSell WHERE id = :id")
    fun getProductSell(id: Long): LiveData<ProductSell>

    @Query("SELECT * FROM productSell WHERE sell_id = :id")
    fun getProductSellByIdSell(id: Long): LiveData<List<ProductSell>>

    @Query("DELETE from productSell")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteProductSell(product: ProductSell)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListProductSell(productsSell: List<ProductSell>)

}