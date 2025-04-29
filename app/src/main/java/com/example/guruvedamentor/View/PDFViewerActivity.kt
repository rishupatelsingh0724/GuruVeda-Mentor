package com.example.guruvedamentor.View

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guruvedamentor.R
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class PDFViewerActivity : AppCompatActivity() {
    lateinit var pdfView: PDFView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pdfviewer)

        pdfView = findViewById(R.id.pdfView)

        val pdfUrl = intent.getStringExtra("pdfUrl") ?: ""

        statusBar()
        if (pdfUrl.isNotEmpty()) {
            downloadAndDisplayPdf(pdfUrl)
        }

    }

    fun statusBar(){
        val statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.statusBarColor = statusBarColor

    }

    private fun downloadAndDisplayPdf(pdfUrl: String) {
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)
        val localFile = File.createTempFile("tempPdf", ".pdf")

        storageReference.getFile(localFile)
            .addOnSuccessListener {
                pdfView.fromFile(localFile)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .load()
            }
            .addOnFailureListener { exception ->
                Log.e("PDFViewerActivity", "Error downloading file", exception)
            }
    }

}