package com.auu.hunterblade.almacen.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.auu.hunterblade.almacen.data.Product
import com.auu.hunterblade.almacen.data.Sell
import com.auu.hunterblade.almacen.databinding.ListItemProductsBinding
import com.auu.hunterblade.almacen.databinding.ListItemSellsBinding
import com.auu.hunterblade.almacen.ui.fragments.products.ProductsFragmentDirections

class ListSellsAdapter : ListAdapter<Sell, RecyclerView.ViewHolder>(ListSellDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SellViewHolder(
            ListItemSellsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sell = getItem(position)
        (holder as SellViewHolder).bind(sell)
    }

    class SellViewHolder(
        private val binding: ListItemSellsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.sell?.let { item ->
//                    navigateToSell(item, it)
                }
            }
        }

//        private fun navigateToSell(
//            item: Sell,
//            view: View
//        ) {
//            val direction =
//                ProductsFragmentDirections.actionNavigationProductListToNavigationProductDetail(
//                    item.idProducto
//                )
//            view.findNavController().navigate(direction)
//        }

        fun bind(item: Sell) {
            binding.apply {
                sell = item
                executePendingBindings()
            }
        }
    }
}

private class ListSellDiffCallback : DiffUtil.ItemCallback<Sell>() {

    override fun areItemsTheSame(oldItem: Sell, newItem: Sell): Boolean {
        return oldItem.idSell == newItem.idSell
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Sell, newItem: Sell): Boolean {
        return oldItem == newItem
    }
}