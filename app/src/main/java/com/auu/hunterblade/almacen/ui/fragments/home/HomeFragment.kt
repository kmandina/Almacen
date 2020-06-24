package com.auu.hunterblade.almacen.ui.fragments.home

import android.graphics.Color.red
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.databinding.FragmentHomeBinding
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence

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

        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val started = sp.getBoolean("startedMain", false)

        if(!started){
            getSequence(cv1, cv2)
                .listener(object : TapTargetSequence.Listener {

                    override fun onSequenceFinish() {                  //  ((TextView) findViewById(R.id.educated)).setText("Congratulations! You're educated now!");

                    }

                    //
                    override fun onSequenceStep(lastTarget: TapTarget, targetClicked: Boolean) {


                    }

                    //
                    override fun onSequenceCanceled(lastTarget: TapTarget) {
                        // /*
                    }
                })
                .start()
            sp.edit().putBoolean("startedMain", true).apply()
        }
    }

    private fun getSequence(cv1: CardView, cv2: CardView): TapTargetSequence {

        val textColor = R.color.colorBackgroundMain
        val outerCircleColor = R.color.colorAccent

        return TapTargetSequence(activity)
            .targets( // Likewise, this tap target will target the search button
                TapTarget.forView(cv1, "Titulo", "Descripcion").outerCircleColor(outerCircleColor) // Specify a color for the outer circle
                    .outerCircleAlpha(0.70f) // Specify the alpha amount for the outer circle
                    .targetCircleColor(textColor) // Specify a color for the target circle
                    .titleTextSize(22) // Specify the size (in sp) of the title text
                    .titleTextColor(textColor)
                    .descriptionTextSize(16) // Specify the size (in sp) of the description text
                    .descriptionTextColor(outerCircleColor)  // Specify the color of the description text
                    .textColor(textColor) // Specify a color for both the title and description text
                    .textTypeface(Typeface.SANS_SERIF) // Specify a typeface for the text
                    .dimColor(R.color.colorPrimary) // If set, will dim behind the view with 30% opacity of the given color
                    .drawShadow(true) // Whether to draw a drop shadow or not
                    .cancelable(true) // Whether tapping outside the outer circle dismisses the view
                    .tintTarget(true) // Whether to tint the target view's color
                    .transparentTarget(false) // Specify whether the target is transparent (displays the content underneath)
                    .targetRadius(60),
                TapTarget.forView(cv2,"Titulo", "Descripcion") // All options below are optional
                    .outerCircleColor(outerCircleColor) // Specify a color for the outer circle
                    .outerCircleAlpha(0.70f) // Specify the alpha amount for the outer circle
                    .targetCircleColor(textColor) // Specify a color for the target circle
                    .titleTextSize(22) // Specify the size (in sp) of the title text
////.titleTextColor(R.color.White)      // Specify the color of the title text
                    .descriptionTextSize(16) // Specify the size (in sp) of the description text
////.descriptionTextColor(R.color.red)  // Specify the color of the description text
                    .textColor(textColor) // Specify a color for both the title and description text
                    .textTypeface(Typeface.SANS_SERIF) // Specify a typeface for the text
                    .dimColor(R.color.colorPrimary) // If set, will dim behind the view with 30% opacity of the given color
                    .drawShadow(true) // Whether to draw a drop shadow or not
                    .cancelable(true) // Whether tapping outside the outer circle dismisses the view
                    .tintTarget(true) // Whether to tint the target view's color
                    .transparentTarget(false) // Specify whether the target is transparent (displays the content underneath)
//// Specify a custom drawable to draw as the target
                    .targetRadius(60)
            )

    }

}
