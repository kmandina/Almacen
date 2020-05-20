package com.auu.hunterblade.almacen.data

import androidx.room.Embedded
import androidx.room.Relation

data class SellAndProductSell (
    @Embedded
    val sell: Sell,

    @Relation(parentColumn = "id", entityColumn = "sell_id")
    val products: List<ProductSell> = emptyList()
)