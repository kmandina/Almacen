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
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.databinding.FragmentSellViewBinding
import com.auu.hunterblade.almacen.ui.adapters.ListProdsSellAdapter
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel
import com.auu.hunterblade.almacen.utils.InjectorUtils
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class SellFragment : Fragment() {


    private val args: SellFragmentArgs by navArgs()

    private val viewModel: SellViewModel by viewModels {
        InjectorUtils.provideSellViewModelFactory(requireActivity(), args.id)
    }

    private val viewModelProducts: ProductViewModel by viewModels {
        InjectorUtils.provideProductViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSellViewBinding.inflate(inflater, container, false)

        context ?: return binding.root

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.prodSells)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)
        val tvEarn = view.findViewById<TextView>(R.id.tvEarn)
        val addProdSell = view.findViewById<FloatingActionButton>(R.id.addProdSell)
        val addNote = view.findViewById<FloatingActionButton>(R.id.addNote)
        val addSave = view.findViewById<FloatingActionButton>(R.id.addSave)

        var bandera = true

        viewModel.sell.observe(viewLifecycleOwner){

            val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)

            tvDate.text =  dateFormat.format(it.date.time)
            tvEarn.text = getString(R.string.product_earn) + " $${it.totalEarn}"

            if(bandera) {
                val adapter = ListProdsSellAdapter(viewLifecycleOwner, viewModel, viewModelProducts, it)
                recyclerView.adapter = adapter
                subscribeUi(adapter)
                bandera = false
            }

            if(recyclerView.adapter != null) {
                (recyclerView.adapter as ListProdsSellAdapter).submitSell(it)
            }
        }

        addProdSell.setOnClickListener {

            it.findNavController().navigate(
                SellFragmentDirections.actionNavigationSellViewToSellProductRegister(args.id)
            )
        }

        addNote.setOnClickListener {

            viewModel.sell.let { sellt ->

                if (sellt.value != null) {

                    val sell = sellt.value!!

                    val d = Dialog(requireActivity())
                    d.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    d.setContentView(R.layout.sell_dialog_modify)
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

                    val cal = Calendar.getInstance()

                    calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

                        cal.set(year, month, dayOfMonth)

                    }

                    calendarView.setDate(sell.date.time.time, true, true)

                    note.setText(sell.note)
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

                    cancelSell.setOnClickListener { d.dismiss() }

                    acceptSell.setOnClickListener {

                        if (note.text == null || note.text.toString() == "") {

                            sell.note = ""
                            sell.date = cal

                            viewModel.updateSell(sell)
                            d.dismiss()

                        } else if (Validador()) {

                            sell.note = note.text.toString()
                            sell.date = cal

                            viewModel.updateSell(sell)
                            d.dismiss()

                        }

                    }
//                viewModel.sell.removeObservers(viewLifecycleOwner)
                }
            }
        }

        addSave.setOnClickListener {v ->

            viewModel.sell.let {

                if(it.value != null) {
                    val sell = it.value!!
                    val direction =
                        SellFragmentDirections.actionNavigationSellViewToReportSell(sell.idSell)
                    v.findNavController().navigate(direction)
                }
            }
        }

        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val started = sp.getBoolean("startedSell", false)

        if(!started){
            getSequence(addProdSell, addNote)
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
            sp.edit().putBoolean("startedSell", true).apply()
        }

    }

    private fun getSequence(addProdSell: FloatingActionButton, addNote: FloatingActionButton): TapTargetSequence {

        val textColor = R.color.colorBackgroundMain
        val outerCircleColor = R.color.colorAccent

        return TapTargetSequence(activity)
            .targets( // Likewise, this tap target will target the search button
                TapTarget.forView(addProdSell, "Titulo", "Descripcion").outerCircleColor(outerCircleColor) // Specify a color for the outer circle
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
                TapTarget.forView(addNote,"Titulo", "Descripcion") // All options below are optional
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


    private fun subscribeUi(adapter: ListProdsSellAdapter) {
        viewModel.sellList.observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)
        }
    }

}