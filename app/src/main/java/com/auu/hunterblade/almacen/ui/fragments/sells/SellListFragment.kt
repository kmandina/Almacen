package com.auu.hunterblade.almacen.ui.fragments.sells

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.data.Sell
import com.auu.hunterblade.almacen.databinding.FragmentListSellBinding
import com.auu.hunterblade.almacen.ui.adapters.ListSellsAdapter
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel
import com.auu.hunterblade.almacen.utils.InjectorUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SellListFragment : Fragment() {

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

                if(note.text == null || note.text.toString() == ""){

                    viewModel.lista.observe(viewLifecycleOwner){ list ->

                        if(bandera && list.isNotEmpty()){

                            view.findNavController().navigate(
                                SellListFragmentDirections.actionNavigationSellListToSellView(list.first().idSell)
                            )
                        }
                    }

                    viewModel.addSell(Sell(note = ""))
                    d.dismiss()
                }else if(Validador()){
                    viewModel.lista.observe(viewLifecycleOwner){ list ->

                        if(bandera && list.isNotEmpty()){

                            view.findNavController().navigate(
                                SellListFragmentDirections.actionNavigationSellListToSellView(list.first().idSell)
                            )
                        }
                    }

                    viewModel.addSell(Sell(note = note.text.toString()))
                    d.dismiss()
                }
                bandera = true
            }

        }
    }

    private fun subscribeUi(
        adapter: ListSellsAdapter
    ) {
        viewModel.lista.observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)
        }
    }
}