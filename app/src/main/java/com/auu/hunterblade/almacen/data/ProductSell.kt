package com.auu.hunterblade.almacen.data

import androidx.room.*

@Entity(
    tableName = "productSell",
    foreignKeys = [
        ForeignKey(entity = Sell::class, parentColumns = ["id"], childColumns = ["sell_id"])
    ],
    indices = [Index("sell_id")]
)
data class ProductSell (

    @ColumnInfo(name = "sell_id") val idSell: Long,

    @ColumnInfo(name = "product_id") val idProduct: Long,

    @ColumnInfo(name = "name") val nameProduct: String,

    @ColumnInfo(name = "amountSell") val amountSell: Long,

    @ColumnInfo(name = "earnSell") val earnSell: Float
){
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var idProductSell: Long = 0
}