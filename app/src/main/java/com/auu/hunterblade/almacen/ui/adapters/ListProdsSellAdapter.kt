package com.auu.hunterblade.almacen.ui.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.data.ProductSell
import com.auu.hunterblade.almacen.data.Sell
import com.auu.hunterblade.almacen.databinding.ListItemProductsSellsBinding
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel
import com.auu.hunterblade.almacen.ui.fragments.sells.SellViewModel

class ListProdsSellAdapter(
    private val life: LifecycleOwner,
    val viewModel: SellViewModel,
    val prods: ProductViewModel,
    var sell: Sell
) : ListAdapter<ProductSell, RecyclerView.ViewHolder>(ListProdSellDiffCallback()) {

    fun submitSell(sell: Sell){
        this.sell = sell
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductSellViewHolder(
            ListItemProductsSellsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val prod = getItem(position)
        (holder as ProductSellViewHolder).bind(prod)

        holder.itemView.setOnLongClickListener {

            val context = it.context

            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.notification))
                .setMessage(context.getString(R.string.alert_delete))
                .setNegativeButton(context.getString(R.string.cancel)) { _, _ ->   }
                .setPositiveButton(context.getString(R.string.accept)) { _, _ ->

                    viewModel.deleteSell(prod, sell)

                    val p = prods.getProduct(prod.idProduct)

                    p.observe(life) { pro ->

                        prods.updateProductById(prod.idProduct, pro.amount + prod.amountSell)
                        p.removeObservers(life)

                    }
                }
                .show()

            true
        }

//        holder.itemView.setOnLongClickListener {
//
//            val context = it.context
//
//            AlertDialog.Builder(context)
//                .setTitle(context.getString(R.string.notification))
//                .setMessage(context.getString(R.string.alert_action))
//                .setNegativeButton(context.getString(R.string.delete)) { _, _ ->  viewModel.deleteProduct(prod) }
//                .setNeutralButton(context.getString(R.string.share)){_,_ ->   createShareIntent(prod, context) }
//                .setPositiveButton(context.getString(R.string.cancel)) { _, _ -> }
//                .show()
//
//            true
//        }

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