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
import com.auu.hunterblade.almacen.data.ProductSell
import com.auu.hunterblade.almacen.databinding.ListItemProductsBinding
import com.auu.hunterblade.almacen.databinding.ListItemProductsSellsBinding
import com.auu.hunterblade.almacen.ui.fragments.products.ProductsFragmentDirections

class ListProdsSellAdapter : ListAdapter<ProductSell, RecyclerView.ViewHolder>(ListProdSellDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductSellViewHolder(
            ListItemProductsSellsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val prod = getItem(position)
        (holder as ProductSellViewHolder).bind(prod)
    }

    class ProductSellViewHolder(
        private val binding: ListItemProductsSellsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
//            binding.setClickListener {
//                binding.product?.let { item ->
//                    navigateToProduct(item, it)
//                }
//            }
        }

//        private fun navigateToProduct(
//            item: ProductSell,
//            view: View
//        ) {
//            val direction =
//                ProductsFragmentDirections.actionNavigationProductListToNavigationProductDetail(
//                    item.idProducto
//                )
//            view.findNavController().navigate(direction)
//        }

        fun bind(item: ProductSell) {
            binding.apply {
                product = item
                executePendingBindings()
            }
        }
    }
}

private class ListProdSellDiffCallback : DiffUtil.ItemCallback<ProductSell>() {

    override fun areItemsTheSame(oldItem: ProductSell, newItem: ProductSell): Boolean {
        return oldItem.idProductSell == newItem.idProductSell
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ProductSell, newItem: ProductSell): Boolean {
        return oldItem == newItem
    }
}