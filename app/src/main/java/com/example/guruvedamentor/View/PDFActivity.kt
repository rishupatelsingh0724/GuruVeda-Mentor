package com.example.guruvedamentor.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.Adapters.PDFAdapter
import com.example.guruvedamentor.DataModel.PdfModel
import com.example.guruvedamentor.Interface.PDFUpdateAndDelete
import com.example.guruvedamentor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PDFActivity : AppCompatActivity(), PDFUpdateAndDelete {
    lateinit var pdfRecyclerView: RecyclerView
    lateinit var pdfAdapter: PDFAdapter
    lateinit var pdfList: ArrayList<PdfModel>
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var pdfRecyclerViewProgressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pdfactivity)

        db= FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()

        var category=intent.getStringExtra("category")

        pdfList = ArrayList()
        pdfAdapter = PDFAdapter(pdfList,this)
        pdfRecyclerView = findViewById(R.id.pdfRecyclerView)
        pdfRecyclerViewProgressBar = findViewById(R.id.pdfRecyclerViewProgressBar)
        pdfRecyclerViewProgressBar.visibility = android.view.View.VISIBLE


        pdfRecyclerView.layoutManager = LinearLayoutManager(this)
        pdfRecyclerView.adapter = pdfAdapter


        category?.let {
            getPdf(it)
        }
        statusBar()


    }

    @SuppressLint("NotifyDataSetChanged")
    fun getPdf(category: String){

        db.collection("pdfs").whereEqualTo("category",category).whereEqualTo("teacherId",auth.currentUser?.uid).get().addOnSuccessListener {
            pdfList.clear()
            for (document in it){
                val pdfModel=document.toObject(PdfModel::class.java)
                pdfList.add(pdfModel)
            }
            pdfRecyclerViewProgressBar.visibility = android.view.View.GONE
            pdfAdapter.notifyDataSetChanged()
        }


    }


    fun statusBar(){
        val statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.statusBarColor = statusBarColor

    }

    override fun onUpdateClicked(
        position: Int,
        pdfModel: PdfModel
    ) {
        val intent=Intent(this,UpdatePDFActivity::class.java)
        intent.putExtra("pdfId",pdfModel.pdfId)
        intent.putExtra("pdfTitle",pdfModel.pdfTitle)
        intent.putExtra("pdfUrl",pdfModel.pdfUrl)
        startActivity(intent)

    }

    override fun onDeleteClicked(
        position: Int,
        pdfModel: PdfModel
    ) {

        val alertDialog=AlertDialog.Builder(this)
        alertDialog.setTitle("Delete PDF")
        alertDialog.setMessage("Are you sure you want to delete this PDF?")
        alertDialog.setPositiveButton("Delete") { _, _ ->
            db.collection("pdfs").document(pdfModel.pdfId).delete()
            pdfList.removeAt(position)
            pdfAdapter.notifyItemRemoved(position)
            pdfAdapter.notifyItemRangeChanged(position, pdfList.size)
        }
        alertDialog.setNegativeButton("Cancel") { _, _ ->
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
        alertDialog.show()


    }

}