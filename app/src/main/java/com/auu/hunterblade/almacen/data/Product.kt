package com.auu.hunterblade.almacen.data

import androidx.room.*

@Entity(tableName = "product")
data class Product(

    @ColumnInfo(name = "name") val name: String,

    @ColumnInfo(name = "description") val description: String,

    @ColumnInfo(name = "price_buy") val priceBuy: Double,

    @ColumnInfo(name = "price_sell") val priceSell: Double,

    @ColumnInfo(name = "photo") val photo: String,

    @ColumnInfo(name = "amount") val amount: Long
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var idProducto: Long = 0
}
