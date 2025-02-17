package com.auu.hunterblade.almacen.ui.fragments.sells

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.data.Sell
import com.auu.hunterblade.almacen.databinding.FragmentListSellBinding
import com.auu.hunterblade.almacen.ui.adapters.ListSellsAdapter
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel
import com.auu.hunterblade.almacen.utils.InjectorUtils
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class SellListFragment : Fragment() {

    /* TODO:
    *   Day Picker
    *
    *
    *  */


    private val viewModel: SellListViewModel by viewModels {
        InjectorUtils.provideSellListViewModelFactory(requireActivity())
    }

    private val viewModelProducts: ProductViewModel by viewModels {
        InjectorUtils.provideProductViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentListSellBinding.inflate(inflater, container, false)

        context ?: return binding.root

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.sells)
        val tvEarn = view.findViewById<TextView>(R.id.tvEarn)
        val addSell = view.findViewById<FloatingActionButton>(R.id.addSell)

        val adapter = ListSellsAdapter(viewModel, viewModelProducts, viewLifecycleOwner)
        recycler.adapter = adapter
        subscribeUi(adapter)

        viewModel.sum.observe(viewLifecycleOwner) {

            if (it != null) {
                tvEarn.text = "$$it"
            }else {
                tvEarn.text= "$0"
            }
        }

        addSell.setOnClickListener {

            var bandera = false

            val d = Dialog(requireActivity())
            d.requestWindowFeature(Window.FEATURE_NO_TITLE)
            d.setContentView(R.layout.sell_dialog_insert)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(d.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            d.window!!.attributes = lp
            d.show()

            val note: EditText = d.findViewById(R.id.etSellNote)
            val acceptSell = d.findViewById<Button>(R.id.acceptSell)
            val cancelSell = d.findViewById<Button>(R.id.cancelSell)
            val calendarView = d.findViewById<CalendarView>(R.id.calendar)

            note.requestFocus()

            fun Validador(): Boolean {
                var validado = true

                if (note.text.toString().length > 20) {
                    validado = false
                    note.requestFocus()
                    Toast.makeText(
                        context,
                        getString(R.string.alert_overflow_note),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                return validado
            }

            val cal = Calendar.getInstance()

            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

                cal.set(year, month, dayOfMonth)

            }

            cancelSell.setOnClickListener { d.dismiss() }

            acceptSell.setOnClickListener {

                if(note.text == null || note.text.toString() == ""){

                    viewModel.lista.observe(viewLifecycleOwner){ list ->

                        if(bandera && list.isNotEmpty()){

                            view.findNavController().navigate(
                                SellListFragmentDirections.actionNavigationSellListToSellView(list.first().idSell)
                            )
                        }
                    }

                    viewModel.addSell(Sell(note = "", date = cal))
                    d.dismiss()
                }else if(Validador()){
                    viewModel.lista.observe(viewLifecycleOwner){ list ->

                        if(bandera && list.isNotEmpty()){

                            view.findNavController().navigate(
                                SellListFragmentDirections.actionNavigationSellListToSellView(list.first().idSell)
                            )
                        }
                    }

                    viewModel.addSell(Sell(note = note.text.toString(), date = cal))
                    d.dismiss()
                }
                bandera = true
            }

        }

        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val started = sp.getBoolean("startedSells", false)

        if(!started){
            getSequence(addSell)
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
            sp.edit().putBoolean("startedSells", true).apply()
        }
    }

    private fun getSequence(addSell: FloatingActionButton): TapTargetSequence {

        val textColor = R.color.colorBackgroundMain
        val outerCircleColor = R.color.colorAccent

        return TapTargetSequence(activity)
            .targets( // Likewise, this tap target will target the search button
                TapTarget.forView(addSell, "Titulo", "Descripcion").outerCircleColor(outerCircleColor) // Specify a color for the outer circle
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
                    .targetRadius(60)
            )

    }


    private fun subscribeUi(
        adapter: ListSellsAdapter
    ) {
        viewModel.lista.observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)
        }
    }
}