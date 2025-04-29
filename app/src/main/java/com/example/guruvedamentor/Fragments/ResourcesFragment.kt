package com.example.guruvedamentor.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.guruvedamentor.R
import com.example.guruvedamentor.View.PDFActivity
import com.example.guruvedamentor.View.UploadResourcesActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ResourcesFragment : Fragment() {
    lateinit var addPDF: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_resources, container, false)

        addPDF = view.findViewById(R.id.addPDF)
        val kotlinPDF=view.findViewById<CardView>(R.id.kotlinPDF)
        val javaPDF=view.findViewById<CardView>(R.id.javaPDF)
        val dartPDF=view.findViewById<CardView>(R.id.dartPDF)

        addPDF.setOnClickListener {

            val intent = Intent(requireContext(), UploadResourcesActivity::class.java)
            startActivity(intent)

        }

        kotlinPDF.setOnClickListener {
            val intent = Intent(requireContext(), PDFActivity::class.java)
            intent.putExtra("category","Kotlin")
            startActivity(intent)
        }

        javaPDF.setOnClickListener {
            val intent = Intent(requireContext(), PDFActivity::class.java)
            intent.putExtra("category","Java")
            startActivity(intent)
        }

        dartPDF.setOnClickListener {
            val intent = Intent(requireContext(), PDFActivity::class.java)
            intent.putExtra("category", "Dart")
            startActivity(intent)
        }


        return view


    }

}