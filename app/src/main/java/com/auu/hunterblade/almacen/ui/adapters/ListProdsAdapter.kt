package com.auu.hunterblade.almacen.ui.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.auu.hunterblade.almacen.BuildConfig
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.data.Product
import com.auu.hunterblade.almacen.databinding.ListItemProductsBinding
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel
import com.auu.hunterblade.almacen.ui.fragments.products.ProductsFragmentDirections
import java.io.File

class ListProdsAdapter(
    val viewModel: ProductViewModel,
    val requireActivity: FragmentActivity
) : ListAdapter<Product, RecyclerView.ViewHolder>(ListProdDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(ListItemProductsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val prod = getItem(position)
        (holder as ProductViewHolder).bind(prod)

        holder.itemView.setOnLongClickListener {

            val context = it.context

            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.notification))
                .setMessage(context.getString(R.string.alert_action))
                .setNegativeButton(context.getString(R.string.delete)) { _, _ ->  viewModel.deleteProduct(prod) }
                .setNeutralButton(context.getString(R.string.share)){_,_ ->   createShareIntent(prod, context) }
                .setPositiveButton(context.getString(R.string.cancel)) { _, _ -> }
                .show()

            true
        }


    }

    @Suppress("DEPRECATION")
    private fun createShareIntent(p: Product, context: Context) {

        val file = File(p.photo)
        if (file.exists()) {
            val whatsappIntent = Intent(Intent.ACTION_SEND)
            whatsappIntent.type = "image/*"
            whatsappIntent.putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.share_name) + p.name + "\n"
                        + context.getString(R.string.share_description) + p.description + "\n"
                        + context.getString(R.string.share_price) + p.priceSell + " ${p.currency}"
            )
            val imageUri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",  //(use your app signature + ".provider" )
                file
            )
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, imageUri) //add image path
            whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(
                Intent.createChooser(
                    whatsappIntent,
                    context.getString(R.string.alert_selection_sharing)
                )
            )
        } else {
            val shareIntent = ShareCompat.IntentBuilder.from(requireActivity)
                .setText(context.getString(R.string.share_name) + p.name + "\n"
                        + context.getString(R.string.share_description) + p.description + "\n"
                        + context.getString(R.string.share_price) + p.priceSell + " ${p.currency}")
                .setType("text/plain")
                .createChooserIntent()
                .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            context.startActivity(shareIntent)
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