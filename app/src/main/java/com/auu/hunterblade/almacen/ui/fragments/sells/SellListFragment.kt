package com.auu.hunterblade.almacen.ui.fragments.sells

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.auu.hunterblade.almacen.data.Sell
import com.auu.hunterblade.almacen.databinding.FragmentListSellBinding
import com.auu.hunterblade.almacen.ui.adapters.ListSellsAdapter
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

        viewModel.sum.observe(viewLifecycleOwner) {

            if (it != null) {
                binding.tvEarn.text = "$$it"
            }else {
                binding.tvEarn.text= "$0"
            }
        }

        binding.addProd.setOnClickListener {

            var bandera = false

            viewModel.lista.observe(viewLifecycleOwner){ list ->

                if(bandera && list.isNotEmpty()){

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

    private fun subscribeUi(
        adapter: ListSellsAdapter
    ) {
        viewModel.lista.observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)
        }
    }
}