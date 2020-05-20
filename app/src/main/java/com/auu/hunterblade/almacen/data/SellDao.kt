package com.auu.hunterblade.almacen.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sell: Sell)

    @Query("SELECT * from sell ORDER BY id ASC")
    fun getAllSell(): LiveData<List<Sell>>

    @Query("SELECT * FROM sell WHERE id = :id")
    fun getSell(id: Long): LiveData<Sell>

    @Query("DELETE from sell")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteSell(sell: Sell)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListSell(products: List<Sell>)

    @Transaction
    @Query("SELECT * FROM sell WHERE id IN (SELECT sell_id FROM productSell)")
    fun getSellProducts(): LiveData<List<SellAndProductSell>>

    @Transaction
    @Query("SELECT * FROM sell WHERE id = :id AND id IN (SELECT sell_id FROM productSell)")
    fun getSellProductsById(id: Long): LiveData<SellAndProductSell>

}