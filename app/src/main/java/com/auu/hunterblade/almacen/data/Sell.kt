package com.auu.hunterblade.almacen.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sell")
data class Sell (

    @ColumnInfo(name = "date") val date: Calendar = Calendar.getInstance(),

    @ColumnInfo(name = "totalEarn") val totalEarn: Double = 0.0

){
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var idSell: Long = 0
}