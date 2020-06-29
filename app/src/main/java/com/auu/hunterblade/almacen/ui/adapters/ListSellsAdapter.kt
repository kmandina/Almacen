package com.auu.hunterblade.almacen.ui.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.data.Sell
import com.auu.hunterblade.almacen.databinding.ListItemSellsBinding
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel
import com.auu.hunterblade.almacen.ui.fragments.sells.SellListFragmentDirections
import com.auu.hunterblade.almacen.ui.fragments.sells.SellListViewModel

class ListSellsAdapter(
    val viewModel: SellListViewModel,
    private val viewModelProducts: ProductViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : ListAdapter<Sell, RecyclerView.ViewHolder>(ListSellDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SellViewHolder(
            ListItemSellsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sell = getItem(position)
        (holder as SellViewHolder).bind(sell)

        holder.itemView.setOnLongClickListener {

            val context = it.context

            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.notification))
                .setMessage(context.getString(R.string.alert_action))
                .setNegativeButton(context.getString(R.string.delete)) { _, _ ->   viewModel.deleteSell(sell) }
                .setNeutralButton(context.getString(R.string.action_generate_pdf)){_,_ ->

                    val direction =
                        SellListFragmentDirections.actionNavigationSellListToReportSell(sell.idSell)
                    it.findNavController().navigate(direction)

                }
                .setPositiveButton(context.getString(R.string.cancel)) { _, _ -> }
                .show()

            true
        }
    }

    class SellViewHolder(
        val binding: ListItemSellsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.sell?.let { item ->
                    navigateToSell(item, it)
                }
            }
        }

        private fun navigateToSell(
            item: Sell,
            view: View
        ) {

            val direction =
                SellListFragmentDirections.actionNavigationSellListToSellView(item.idSell)
            view.findNavController().navigate(direction)
        }

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