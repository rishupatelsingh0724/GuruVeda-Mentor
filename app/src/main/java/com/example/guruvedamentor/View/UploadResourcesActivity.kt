package com.example.guruvedamentor.View

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.guruvedamentor.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class UploadResourcesActivity : AppCompatActivity() {
    lateinit var pickPdfButton: AppCompatButton
    lateinit var uploadProgressBar: ProgressBar
    lateinit var statusText: TextView
    lateinit var titleText: EditText
    private val PDF_PICK_CODE = 1000
    lateinit var categorySpinner:Spinner
    lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_upload_resources)

        storageReference = FirebaseStorage.getInstance().reference

        pickPdfButton = findViewById(R.id.pickPdfButton)
        uploadProgressBar = findViewById(R.id.uploadProgressBar)
        statusText = findViewById(R.id.statusText)
        titleText = findViewById(R.id.titleText)
        categorySpinner= findViewById(R.id.categorySpinner)


        pickPdfButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, PDF_PICK_CODE)
        }

        val categories = listOf("Kotlin", "Java", "Dart")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

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

    @SuppressLint("SetTextI18n")
    private fun uploadPdfToFirebase(pdfUri: Uri) {
        val fileName = "pdfs/${System.currentTimeMillis()}.pdf"
        val id= Timestamp.now().nanoseconds.toString()
        val fileRef = storageReference.child("$fileName/$id")

        uploadProgressBar.visibility = View.VISIBLE
        statusText.text = "Uploading..."

        val uploadTask = fileRef.putFile(pdfUri)
        uploadTask.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                uploadProgressBar.visibility = View.GONE
                val pdfUrl = uri.toString()
                val pdfTitle = titleText.text.toString()
                val db = FirebaseFirestore.getInstance()
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val selectedCategory = categorySpinner.selectedItem.toString()
                val pdfId=UUID.randomUUID().toString()


                val pdfData = hashMapOf(
                    "pdfId" to pdfId,
                    "teacherId" to userId,
                    "pdfTitle" to pdfTitle,
                    "pdfUrl" to pdfUrl,
                    "category" to selectedCategory
                )

                db.collection("pdfs").document(pdfId)
                    .set(pdfData)
                    .addOnSuccessListener {
                        uploadProgressBar.visibility = View.GONE
                        statusText.text = "Upload successful!"
                        titleText.text.clear()
                    }

            }
        }.addOnFailureListener {
            uploadProgressBar.visibility = View.GONE
            statusText.text = "Upload failed: ${it.message}"
        }
    }

}