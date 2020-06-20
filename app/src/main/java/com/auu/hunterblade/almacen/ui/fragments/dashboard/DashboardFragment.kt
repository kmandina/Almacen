package com.auu.hunterblade.almacen.ui.fragments.dashboard

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.data.ProductSell
import com.auu.hunterblade.almacen.databinding.FragmentDashboardBinding
import com.auu.hunterblade.almacen.ui.adapters.ListSellsAdapter
import com.auu.hunterblade.almacen.utils.InjectorUtils
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*

class DashboardFragment : Fragment() {

    private val viewModel: ProductSellViewModel by viewModels {
        InjectorUtils.provideProductSellListViewModelFactory(requireActivity())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDashboardBinding.inflate(inflater, container, false)

        context ?: return binding.root

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chart = view.findViewById<PieChart>(R.id.pieChart1)

        chart.description.isEnabled = false

        val tf = Typeface.createFromAsset(requireContext().assets, "OpenSans-Light.ttf")

        chart.setCenterTextTypeface(tf)
        chart.centerText = generateCenterText()
        chart.setCenterTextSize(10f)
        chart.setCenterTextTypeface(tf)

        // radius of the center hole in percent of maximum radius

        // radius of the center hole in percent of maximum radius
        chart.holeRadius = 45f
        chart.transparentCircleRadius = 50f

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)

        viewModel.lista.observe(viewLifecycleOwner){ list ->

            chart.data = generatePieDataFromList(list)
            chart.invalidate()
        }
    }

    private fun generateCenterText(): SpannableString {
        val s = SpannableString(getString(R.string.product_sell_title_statistics))
        s.setSpan(RelativeSizeSpan(2f), 0, 8, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 8, s.length, 0)
        return s
    }

    fun generatePieDataFromList(list: List<ProductSell>): PieData {

        val entries1 = ArrayList<PieEntry>()
        for (p in list) {
            entries1.add(PieEntry(p.amountSell.toFloat(), p.nameProduct))
        }
        val ds1 = PieDataSet(entries1, getString(R.string.product_sell_title_statistics))
        ds1.setColors(*ColorTemplate.MATERIAL_COLORS)
        ds1.sliceSpace = 2f
        ds1.valueTextColor = Color.WHITE
        ds1.valueTextSize = 12f
        val d = PieData(ds1)

        val tfText: Typeface = Typeface.createFromAsset(requireContext().assets, "OpenSans-Regular.ttf");

        d.setValueTypeface(tfText)
        return d
    }
}
