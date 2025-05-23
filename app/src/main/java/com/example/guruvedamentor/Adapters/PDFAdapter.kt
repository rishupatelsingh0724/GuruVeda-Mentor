package com.example.guruvedamentor.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.DataModel.PdfModel
import com.example.guruvedamentor.Interface.PDFUpdateAndDelete
import com.example.guruvedamentor.R
import com.example.guruvedamentor.View.PDFViewerActivity

class PDFAdapter(private val pdfList: ArrayList<PdfModel>,val listener: PDFUpdateAndDelete) :RecyclerView.Adapter<PDFAdapter.PDFViewHolder>() {
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
        holder.onClickImageView.setOnClickListener {

                val popup = PopupMenu(holder.itemView.context, holder.onClickImageView)
                popup.inflate(R.menu.question_item_menu)

                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_update -> {
                            listener.onUpdateClicked(position, pdf)
                            true
                        }
                        R.id.menu_delete -> {
                            listener.onDeleteClicked(position, pdf)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PDFViewerActivity::class.java)
            intent.putExtra("pdfUrl", pdf.pdfUrl)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {1
       return pdfList.size
    }

    class PDFViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pdfTittle=itemView.findViewById<TextView>(R.id.pdfTittle)
        val onClickImageView=itemView.findViewById<ImageView>(R.id.onClickImageView)
    }
}