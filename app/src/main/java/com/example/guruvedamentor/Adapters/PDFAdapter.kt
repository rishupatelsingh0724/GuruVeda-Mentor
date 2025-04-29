package com.example.guruvedamentor.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.DataModel.PdfModel
import com.example.guruvedamentor.R
import com.example.guruvedamentor.View.PDFViewerActivity

class PDFAdapter(private val pdfList: ArrayList<PdfModel>) :RecyclerView.Adapter<PDFAdapter.PDFViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PDFViewHolder {
        val layout=LayoutInflater.from(parent.context).inflate(R.layout.pdf_layout,parent,false)
        return PDFViewHolder(layout)
    }

    override fun onBindViewHolder(
        holder: PDFViewHolder,
        position: Int
    ) {
        val pdf=pdfList[position]
        holder.pdfTittle.text=pdf.pdfTitle

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PDFViewerActivity::class.java)
            intent.putExtra("pdfUrl", pdf.pdfUrl)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
       return pdfList.size
    }

    class PDFViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pdfTittle=itemView.findViewById<TextView>(R.id.pdfTittle)
    }
}