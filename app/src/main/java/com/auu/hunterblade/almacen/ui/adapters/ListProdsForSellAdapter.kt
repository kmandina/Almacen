package com.auu.hunterblade.almacen.ui.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.data.Product
import com.auu.hunterblade.almacen.data.ProductSell
import com.auu.hunterblade.almacen.databinding.ListItemForSellBinding
import com.auu.hunterblade.almacen.databinding.ListItemProductsBinding
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel
import com.auu.hunterblade.almacen.ui.fragments.products.ProductsFragmentDirections
import com.auu.hunterblade.almacen.ui.fragments.sells.SellViewModel

class ListProdsForSellAdapter(val id: Long, val viewmodel : SellViewModel, val viewmodelP: ProductViewModel) : ListAdapter<Product, RecyclerView.ViewHolder>(ListProdForSellDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(
            ListItemForSellBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val prod = getItem(position)
        (holder as ProductViewHolder).bind(prod,id, viewmodel, viewmodelP)
    }

    class ProductViewHolder(
        private val binding: ListItemForSellBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { nav ->
                binding.product?.let { item ->

                    val d = Dialog(binding.root.context)
                    d.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    d.setContentView(R.layout.product_sell_dialog_insert)
                    val lp = WindowManager.LayoutParams()
                    lp.copyFrom(d.window!!.attributes)
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                    d.window!!.attributes = lp
                    d.show()

                    val amount: EditText = d.findViewById(R.id.etAmountProduct)
                    val acceptProduct = d.findViewById<Button>(R.id.acceptProduct)
                    val cancelProduct = d.findViewById<Button>(R.id.cancelProduct)

                    fun Validador(): Boolean {
                        var validado = true

                        if (amount.text.toString().isEmpty()) {
                            validado = false
                            amount.requestFocus()
                            Toast.makeText(
                                binding.root.context,
                                binding.root.context.getString(R.string.alert_empty_amount),
                                Toast.LENGTH_SHORT
                            ).show()
                        }else if(amount.text.toString().isNotEmpty() && amount.text.toString().toLong() > item.amount){
                            validado = false
                            amount.requestFocus()
                            Toast.makeText(
                                binding.root.context,
                                binding.root.context.getString(R.string.alert_overflow_amount),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        return validado
                    }

                    cancelProduct.setOnClickListener { d.dismiss() }

                    acceptProduct.setOnClickListener {

                        if(Validador()){

                            val earnSell = amount.text.toString().toLong() * item.priceSell - amount.text.toString().toLong() * item.priceBuy

                            val product = ProductSell(binding.id, item.idProducto, item.name, amount.text.toString().toLong(), earnSell)

                            binding.viewmodel?.addProductSell(product,binding.viewmodel?.sell?.value!!)
                            binding.viewmodelP?.updateProductById(item.idProducto, item.amount - amount.text.toString().toLong())

                            d.dismiss()

                            nav.findNavController().navigateUp()
                        }
                    }




                }
            }
        }

        fun bind(item: Product, idS: Long, viewM: SellViewModel, viewMP: ProductViewModel) {
            binding.apply {
                product = item
                viewmodel = viewM
                viewmodelP = viewMP
                id = idS
                executePendingBindings()
            }
        }
    }
}

private class ListProdForSellDiffCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.idProducto == newItem.idProducto
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}