package com.auu.hunterblade.almacen.ui.fragments.products

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.databinding.FragmentProductDetailBinding
import com.auu.hunterblade.almacen.utils.InjectorUtils

class ProductDetailFragment : Fragment() {

    private val args: ProductDetailFragmentArgs by navArgs()

    private val viewModelP: ProductDetailViewModel by viewModels {
        InjectorUtils.provideProductDetailViewModelFactory(requireActivity(), args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProductDetailBinding.inflate(
            inflater, container, false).apply {
            viewModel = viewModelP
            lifecycleOwner = viewLifecycleOwner

            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_share -> {
                        createShareIntent()
                        true
                    }
                    else -> false
                }
            }
        }
        setHasOptionsMenu(true)

        return binding.root
    }

    // Helper function for calling a share functionality.
    // Should be used when user presses a share button/menu item.
    @Suppress("DEPRECATION")
    private fun createShareIntent() {
//        val shareText = plantDetailViewModel.plant.value.let { plant ->
//            if (plant == null) {
//                ""
//            } else {
//                getString(R.string.share_text_plant, plant.name)
//            }
//        }
        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
            .setText("shareText")
            .setType("text/plain")
            .createChooserIntent()
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        startActivity(shareIntent)
    }

}