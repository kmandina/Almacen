package com.auu.hunterblade.almacen.ui.fragments.sells

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.auu.hunterblade.almacen.databinding.FragmentSellViewBinding
import com.auu.hunterblade.almacen.ui.adapters.ListProdsSellAdapter
import com.auu.hunterblade.almacen.utils.InjectorUtils
import java.text.SimpleDateFormat
import java.util.*

class SellFragment : Fragment() {

    private val args: SellFragmentArgs by navArgs()

    private val viewModel: SellViewModel by viewModels {
        InjectorUtils.provideSellViewModelFactory(requireActivity(), args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSellViewBinding.inflate(inflater, container, false)

        context ?: return binding.root

        val adapter = ListProdsSellAdapter()
        binding.prodSells.adapter = adapter
        subscribeUi(adapter)

        viewModel.sell.observe(viewLifecycleOwner){

            val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)

            binding.tvDate.text =  dateFormat.format(it.date.time)
            viewModel.sell.removeObservers(viewLifecycleOwner)
        }

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        binding.addProdSell.setOnClickListener {

            it.findNavController().navigate(
                SellFragmentDirections.actionNavigationSellViewToSellProductRegister(args.id)
            )
        }

        return binding.root
    }

    private fun subscribeUi(adapter: ListProdsSellAdapter) {
        viewModel.sellList.observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)
        }
    }

}