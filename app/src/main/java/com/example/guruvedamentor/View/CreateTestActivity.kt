package com.example.guruvedamentor.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.guruvedamentor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID
import kotlin.random.Random

class CreateTestActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    val db = FirebaseFirestore.getInstance()

    val userId=FirebaseAuth.getInstance().currentUser!!.uid

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_test)

        val editTextTestTitle = findViewById<EditText>(R.id.editTextTestTitle)
        val editTextTestDescription = findViewById<EditText>(R.id.editTextTestDescription)
        val editTextTestSubject =findViewById<EditText>(R.id.editTextSubjects)
        val editTextTimeLimit = findViewById<EditText>(R.id.editTextTimeDuration)
        val saveButton = findViewById<Button>(R.id.buttonSaveTest)

        saveButton.setOnClickListener {

            val testTitle = editTextTestTitle.text.toString()
            val testDescription = editTextTestDescription.text.toString()
            val testSubject = editTextTestSubject.text.toString()

            val timeLimit = editTextTimeLimit.text.toString()
            val testId = UUID.randomUUID().toString()


            val testData = hashMapOf(
                "testId" to testId,
                "teacherId" to userId,
                "testTitle" to testTitle,
                "testSubject" to testSubject,
                "testDescription" to testDescription,
                "timeDuration" to timeLimit
            )
            db.collection("teacher_tests_schedule").document(testId).set(testData)
                .addOnSuccessListener {

                    Toast.makeText(this, "Test created successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->

                    Toast.makeText(this, "Error creating test: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}