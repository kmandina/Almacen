package com.auu.hunterblade.almacen.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.auu.hunterblade.almacen.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.cv1.setOnClickListener{

            it.findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigationProductList())
        }

        binding.cv2.setOnClickListener {

            it.findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToSellList())
        }

        return binding.root
    }
}
