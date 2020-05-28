package com.auu.hunterblade.almacen.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cv1 = view.findViewById<CardView>(R.id.cv1)
        val cv2 = view.findViewById<CardView>(R.id.cv2)

        cv1.setOnClickListener{

            it.findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigationProductList())
        }

        cv2.setOnClickListener {

            it.findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToSellList())
        }
    }
}
