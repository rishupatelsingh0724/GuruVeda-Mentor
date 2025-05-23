package com.example.guruvedamentor.View

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guruvedamentor.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class UpdatePDFActivity : AppCompatActivity() {
    lateinit var updateTitleText: EditText
    lateinit var updatePickPdfButton: AppCompatButton
    lateinit var updateProgressBar: ProgressBar
    lateinit var updateStatusText: TextView
    private val PDF_PICK_CODE = 1000
    lateinit var storageReference: StorageReference
    lateinit var pdfId:String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_pdfactivity)
        updateTitleText = findViewById(R.id.updateTitleText)
        updatePickPdfButton = findViewById(R.id.updatePickPdfButton)
        updateProgressBar = findViewById(R.id.updateProgressBar)
        updateStatusText = findViewById(R.id.updateStatusText)

        storageReference = FirebaseStorage.getInstance().reference
        updateTitleText.setText(intent.getStringExtra("pdfTitle"))
        pdfId=intent.getStringExtra("pdfId").toString()



        if (updateTitleText.text.toString().isNotEmpty()){
            updatePickPdfButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "application/pdf"
                startActivityForResult(intent, PDF_PICK_CODE)
            }
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PDF_PICK_CODE && resultCode == RESULT_OK && data != null) {
            val pdfUri = data.data
            pdfUri?.let {
                uploadPdfToFirebase(it)
            }
        }
    }

    private fun uploadPdfToFirebase(pdfUri: Uri) {
        val fileName = "pdfs/${System.currentTimeMillis()}.pdf"
        val id= Timestamp.now().nanoseconds.toString()
        val fileRef = storageReference.child("$fileName/$id")

        updateProgressBar.visibility = View.VISIBLE
        updateStatusText.text = "Uploading..."

        val uploadTask = fileRef.putFile(pdfUri)
        uploadTask.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                updateProgressBar.visibility = View.GONE
                val pdfUrl = uri.toString()
                val pdfTitle = updateTitleText.text.toString()
                val db = FirebaseFirestore.getInstance()

                val pdfData = hashMapOf(
                    "pdfTitle" to pdfTitle,
                    "pdfUrl" to pdfUrl,

                )

                db.collection("pdfs").document(pdfId)
                    .update(pdfData as Map<String, Any>)
                    .addOnSuccessListener {
                        updateProgressBar.visibility = View.GONE
                        updateStatusText.text = "Upload successful!"
                        updateTitleText.text.clear()
                        finish()
                    }
                    .addOnFailureListener {
                        updateProgressBar.visibility = View.GONE
                        updateStatusText.text = "Upload failed: ${it.message}"
                    }

            }
        }.addOnFailureListener {
            updateProgressBar.visibility = View.GONE
            updateStatusText.text = "Upload failed: ${it.message}"
        }
    }

}