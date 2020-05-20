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
import androidx.navigation.fragment.navArgs
import com.auu.hunterblade.almacen.databinding.FragmentProductsBinding
import com.auu.hunterblade.almacen.databinding.FragmentSellProductRegisterBinding
import com.auu.hunterblade.almacen.ui.adapters.ListProdsAdapter
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel
import com.auu.hunterblade.almacen.utils.InjectorUtils

class SellProductRegisterFragment : Fragment() {

    private val args: SellProductRegisterFragmentArgs by navArgs()

    private val viewModel: ProductViewModel by viewModels {
        InjectorUtils.provideProductViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSellProductRegisterBinding.inflate(inflater, container, false)

        context ?: return binding.root

        val adapter = ListProdsAdapter()
        binding.products.adapter = adapter
        subscribeUi(adapter)

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