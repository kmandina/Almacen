package com.auu.hunterblade.almacen.ui.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.data.Product
import com.auu.hunterblade.almacen.databinding.ListItemProductsBinding
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel
import com.auu.hunterblade.almacen.ui.fragments.products.ProductsFragmentDirections

class ListProdsAdapter(val viewModel: ProductViewModel) : ListAdapter<Product, RecyclerView.ViewHolder>(ListProdDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(ListItemProductsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val prod = getItem(position)
        (holder as ProductViewHolder).bind(prod)

        holder.binding.ibClear.setOnClickListener {

            val context = it.context

            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.notification))
                .setMessage(context.getString(R.string.alert_delete))
                .setNegativeButton(context.getString(R.string.cancel)) { _, _ ->   }
                .setPositiveButton(context.getString(R.string.accept)) { _, _ ->  viewModel.deleteProduct(prod) }
                .show()

        }
    }

    class ProductViewHolder(
         val binding: ListItemProductsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.product?.let { item ->
                    navigateToProduct(item, it)
                }
            }
        }

        private fun navigateToProduct(
            item: Product,
            view: View
        ) {
            val direction =
                ProductsFragmentDirections.actionNavigationProductListToNavigationProductDetail(
                    item.idProducto
                )
            view.findNavController().navigate(direction)
        }

        fun bind(item: Product) {
            binding.apply {
                product = item
                executePendingBindings()
            }
        }
    }
}

private class ListProdDiffCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.idProducto == newItem.idProducto
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}