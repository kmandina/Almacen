package com.auu.hunterblade.almacen.ui.fragments.products

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.data.Product
import com.auu.hunterblade.almacen.databinding.FragmentProductsBinding
import com.auu.hunterblade.almacen.ui.adapters.ListProdsAdapter
import com.auu.hunterblade.almacen.ui.fragments.home.HomeFragmentDirections
import com.auu.hunterblade.almacen.utils.InjectorUtils

class ProductsFragment : Fragment() {

    private val viewModel: ProductViewModel by viewModels {
        InjectorUtils.provideProductViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProductsBinding.inflate(inflater, container, false)

        context ?: return binding.root

        val adapter = ListProdsAdapter()
        binding.products.adapter = adapter
        subscribeUi(adapter)

        binding.addProd.setOnClickListener {

            val d = Dialog(requireActivity())
            d.requestWindowFeature(Window.FEATURE_NO_TITLE)
            d.setContentView(R.layout.product_dialog_insert)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(d.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            d.window!!.attributes = lp
            d.show()

            val name: EditText = d.findViewById(R.id.etProductName)
            val description: EditText = d.findViewById(R.id.etProductDecription)
            val priceBuy: EditText = d.findViewById(R.id.priceBuy)
            val priceSell: EditText = d.findViewById(R.id.priceSell)
            val amount: EditText = d.findViewById(R.id.amount)
            val acceptProduct = d.findViewById<Button>(R.id.acceptProduct)
            val cancelProduct = d.findViewById<Button>(R.id.cancelProduct)

            fun Validador(): Boolean {
                var validado = true

                if (amount.text.toString().isEmpty()) {
                    validado = false
                    amount.requestFocus()
                    Toast.makeText(
                        context,
                        getString(R.string.alert_empty_amount),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (priceSell.text.toString().isEmpty()) {
                    validado = false
                    priceSell.requestFocus()
                    Toast.makeText(
                        context,
                        getString(R.string.alert_empty_price),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (priceBuy.text.toString().isEmpty()) {
                    validado = false
                    priceBuy.requestFocus()
                    Toast.makeText(
                        context, getString(R.string.alert_empty_price),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (description.text.toString().isEmpty()) {
                    validado = false
                    description.requestFocus()
                    Toast.makeText(
                        context,
                        getString(R.string.alert_empty_description),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (name.text.toString().isEmpty()) {
                    validado = false
                    name.requestFocus()
                    Toast.makeText(
                        context,
                        getString(R.string.alert_empty_name),
                        Toast.LENGTH_SHORT
                    ).show()
                }


                return validado
            }

            cancelProduct.setOnClickListener { d.dismiss() }

            acceptProduct.setOnClickListener {

                if(Validador()){

                    val prod = Product(name.text.toString(), description.text.toString(), priceBuy.text.toString().toFloat(), priceSell.text.toString().toFloat(), "url", amount.text.toString().toLong())

                    viewModel.addProduct(prod)
                    d.dismiss()
                }

            }

        }

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        return binding.root
    }



    private fun subscribeUi(adapter: ListProdsAdapter) {
        viewModel.lista.observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)
        }
    }
}