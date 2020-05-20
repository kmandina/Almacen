package com.auu.hunterblade.almacen.ui.fragments.sells

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
import com.auu.hunterblade.almacen.data.Sell
import com.auu.hunterblade.almacen.databinding.FragmentListSellBinding
import com.auu.hunterblade.almacen.databinding.FragmentProductsBinding
import com.auu.hunterblade.almacen.ui.adapters.ListProdsAdapter
import com.auu.hunterblade.almacen.ui.adapters.ListSellsAdapter
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel
import com.auu.hunterblade.almacen.ui.fragments.products.ProductsFragmentDirections
import com.auu.hunterblade.almacen.utils.InjectorUtils

class SellListFragment : Fragment() {

    private val viewModel: SellListViewModel by viewModels {
        InjectorUtils.provideSellListViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentListSellBinding.inflate(inflater, container, false)

        context ?: return binding.root

        val adapter = ListSellsAdapter()
        binding.sells.adapter = adapter
        subscribeUi(adapter)

        binding.addProd.setOnClickListener {

            var bandera = false

            viewModel.lista.observe(viewLifecycleOwner){ list ->

                if(bandera){
                    it.findNavController().navigate(
                        SellListFragmentDirections.actionNavigationSellListToSellView(list.first().idSell)
                    )
                }
            }

            viewModel.addSell(Sell())
            bandera = true
        }

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        return binding.root
    }



    private fun subscribeUi(adapter: ListSellsAdapter) {
        viewModel.lista.observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)

        }
    }
}