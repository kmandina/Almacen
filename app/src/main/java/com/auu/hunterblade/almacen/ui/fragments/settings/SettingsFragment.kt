package com.auu.hunterblade.almacen.ui.fragments.settings

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.databinding.FragmentSettingsBinding
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SettingsFragment : Fragment() {

    /* TODO:
     *    Modulo para la conversion de CUC -> CUP y viceversa u otras monedas ejemplo Dolar
     *    Menu de alertas para los productos, para informar cuando se estan acabando, mediante poner un limite de la existencia y notificar
    *     Exportar pdf o excel con datos ya sea de las ventas o el estado de inventario de los productos
    *     Exportar la base de datos (backup)
    * */



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSettingsBinding.inflate(inflater, container, false)

        context ?: return binding.root

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cv1 = view.findViewById<CardView>(R.id.cv1)
        val cv2 = view.findViewById<CardView>(R.id.cv2)

        cv1.setOnClickListener{

            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.action_feedback)
                .setMessage(R.string.send_feedback_ask)
                .setNegativeButton(
                    R.string.cancel
                ) { dialogInterface, _ -> dialogInterface.dismiss() }
                .setPositiveButton(R.string.accept) { dialogInterface, _ ->
                    composeEmail(arrayOf(getString(R.string.email_address)), getString(
                        R.string.action_feedback))
                    dialogInterface.dismiss()
                }.show()
        }

        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val started = sp.getBoolean("startedSettings", false)

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
            sp.edit().putBoolean("startedSettings", true).apply()
        }

    }

    fun composeEmail(addresses: Array<String?>?, subject: String?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
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
