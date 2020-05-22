package com.auu.hunterblade.almacen.ui.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.data.ProductSell
import com.auu.hunterblade.almacen.data.Sell
import com.auu.hunterblade.almacen.databinding.ListItemProductsSellsBinding
import com.auu.hunterblade.almacen.ui.fragments.sells.SellViewModel

class ListProdsSellAdapter(
    val viewModel: SellViewModel,
    val sell: Sell
) : ListAdapter<ProductSell, RecyclerView.ViewHolder>(ListProdSellDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductSellViewHolder(
            ListItemProductsSellsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val prod = getItem(position)
        (holder as ProductSellViewHolder).bind(prod)

        holder.binding.ibClear.setOnClickListener {

            val context = it.context

            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.notification))
                .setMessage(context.getString(R.string.alert_delete))
                .setNegativeButton(context.getString(R.string.cancel)) { _, _ ->   }
                .setPositiveButton(context.getString(R.string.accept)) { _, _ ->  viewModel.deleteSell(prod, sell) }
                .show()

        }
    }

    class ProductSellViewHolder(
        val binding: ListItemProductsSellsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

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