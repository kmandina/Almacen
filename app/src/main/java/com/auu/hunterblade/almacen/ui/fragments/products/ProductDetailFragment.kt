package com.auu.hunterblade.almacen.ui.fragments.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.auu.hunterblade.almacen.data.Product
import com.auu.hunterblade.almacen.databinding.FragmentProductDetailBinding
import com.auu.hunterblade.almacen.utils.InjectorUtils

class ProductDetailFragment : Fragment() {

    private val args: ProductDetailFragmentArgs by navArgs()

    private val viewModel: ProductDetailViewModel by viewModels {
        InjectorUtils.provideProductDetailViewModelFactory(requireActivity(), args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        context ?: return binding.root



        return binding.root
    }

    interface Callback {
        fun add(prod: Product?)
    }
}