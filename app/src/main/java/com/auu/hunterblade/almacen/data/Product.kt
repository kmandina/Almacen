package com.auu.hunterblade.almacen.data

import androidx.room.*

@Entity(tableName = "product")
data class Product(

    @ColumnInfo(name = "name") var name: String,

    @ColumnInfo(name = "description") var description: String,

    @ColumnInfo(name = "price_buy") var priceBuy: Float,

    @ColumnInfo(name = "price_sell") var priceSell: Float,

    @ColumnInfo(name = "photo") var photo: String,

    @ColumnInfo(name = "amount") var amount: Long
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var idProducto: Long = 0
}
