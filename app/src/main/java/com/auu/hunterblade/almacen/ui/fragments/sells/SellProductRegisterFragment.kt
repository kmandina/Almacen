package com.auu.hunterblade.almacen.ui.fragments.sells

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.databinding.FragmentSellProductRegisterBinding
import com.auu.hunterblade.almacen.ui.adapters.ListProdsForSellAdapter
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel
import com.auu.hunterblade.almacen.utils.InjectorUtils


class SellProductRegisterFragment : Fragment() {

    private val args: SellProductRegisterFragmentArgs by navArgs()

    private val viewModelProducts: ProductViewModel by viewModels {
        InjectorUtils.provideProductViewModelFactory(requireActivity())
    }

    private val viewModelSell: SellViewModel by viewModels {
        InjectorUtils.provideSellViewModelFactory(requireActivity(), args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSellProductRegisterBinding.inflate(inflater, container, false)

        context ?: return binding.root

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.products)

        viewModelSell.sell.observe(viewLifecycleOwner){

            val adapter = ListProdsForSellAdapter(args.id, viewModelSell, viewModelProducts, findNavController())
            recycler.adapter = adapter
            subscribeUi(adapter)
            viewModelSell.sell.removeObservers(viewLifecycleOwner)

        }


    }

    private fun subscribeUi(adapter: ListProdsForSellAdapter) {
        viewModelProducts.lista.observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)
        }
    }
}