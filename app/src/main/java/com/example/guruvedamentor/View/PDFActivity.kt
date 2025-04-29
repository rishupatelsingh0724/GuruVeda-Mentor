package com.example.guruvedamentor.View

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.Adapters.PDFAdapter
import com.example.guruvedamentor.DataModel.PdfModel
import com.example.guruvedamentor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PDFActivity : AppCompatActivity() {
    lateinit var pdfRecyclerView: RecyclerView
    lateinit var pdfAdapter: PDFAdapter
    lateinit var pdfList: ArrayList<PdfModel>
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pdfactivity)

        db= FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()

        var category=intent.getStringExtra("category")

        pdfList = ArrayList()
        pdfAdapter = PDFAdapter(pdfList)
        pdfRecyclerView = findViewById(R.id.pdfRecyclerView)


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
            pdfAdapter.notifyDataSetChanged()
        }


    }


    fun statusBar(){
        val statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.statusBarColor = statusBarColor

    }

}