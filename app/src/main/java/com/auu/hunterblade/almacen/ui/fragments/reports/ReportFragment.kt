package com.auu.hunterblade.almacen.ui.fragments.reports

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.auu.hunterblade.almacen.databinding.FragmentReportBinding
import com.auu.hunterblade.almacen.ui.fragments.products.ProductViewModel
import com.auu.hunterblade.almacen.ui.fragments.sells.SellViewModel
import androidx.lifecycle.observe
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.utils.InjectorUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ReportFragment : Fragment() {

    private val args: ReportFragmentArgs by navArgs()

    private val viewModel: SellViewModel by viewModels {
        InjectorUtils.provideSellViewModelFactory(requireActivity(), args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentReportBinding.inflate(inflater, container, false)

        context ?: return binding.root

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reportEdit = view.findViewById<EditText>(R.id.report)
        val saveSell = view.findViewById<FloatingActionButton>(R.id.saveSell)

        val content: LiveData<String> = getSellText()

        content.observe(viewLifecycleOwner){

            reportEdit.setText(it)

        }

        saveSell.setOnClickListener {

            @SuppressLint("SimpleDateFormat") val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

            generatePdf(timeStamp, reportEdit.text.toString())

        }

    }

    fun generatePdf(name: String, content: String){

        val storageDir: File = requireContext().getExternalFilesDir(null)!!

        val saveFile = File(storageDir, "$name.pdf")

        val docWriter = PdfWriter(saveFile)
        val document = PdfDocument(docWriter)
        document.writeContents(content)
        document.close()

        Toast.makeText(requireContext(), "El reporte fue guardado en ${saveFile.absolutePath}", Toast.LENGTH_SHORT).show()

    }

    fun PdfDocument.writeContents(content: String){

        val doc = Document(this)
        val font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN)
        val paragraph = Paragraph(content)
            .setFont(font)
            .setFontSize(20f)


        doc.add(paragraph)
        doc.close()

    }

    fun getSellText(): LiveData<String> {

        val response = MutableLiveData<String>()

        var head = ""
        var body = ""

        viewModel.sell.observe(viewLifecycleOwner){

            Log.d("Sell","Value")
            val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
            val fecha = dateFormat.format(it.date.time)

            head = " Fecha: $fecha    Ganancia: ${it.totalEarn} \n \n"

            response.apply {
                value =  head + body
            }
        }

        viewModel.sellList.observe(viewLifecycleOwner){

            Log.d("SellList","Value")
            body = " Nombre     Vendidos    Ganancia \n"
            for(p in it){

                body += " ${p.nameProduct}       ${p.amountSell}        ${p.earnSell} \n"

            }

            response.apply {
                value =  head + body
            }
        }

        return response
    }
}