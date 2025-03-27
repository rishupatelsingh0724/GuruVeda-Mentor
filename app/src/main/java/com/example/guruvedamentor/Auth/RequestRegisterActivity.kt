package com.example.guruvedamentor.Auth

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guruvedamentor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RequestRegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_request_register)

        auth = FirebaseAuth.getInstance()


        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val name = findViewById<EditText>(R.id.etName)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val mobile = findViewById<EditText>(R.id.etMobile)



        val btnRegister = findViewById<AppCompatButton>(R.id.btnRegister)

        btnRegister.setOnClickListener {

            val etEmail=email.text.toString()
            val etName=name.text.toString()
            val etPassword=password.text.toString()
            val etConfirmPassword=etConfirmPassword.text.toString()
            val etMobile=mobile.text.toString()

            if (etPassword == etConfirmPassword) {

                if (etEmail.isNotEmpty() && etPassword.isNotEmpty() && etName.isNotEmpty() && etConfirmPassword.isNotEmpty() && etMobile.isNotEmpty()) {

                    auth.createUserWithEmailAndPassword(etEmail, etPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val uid = auth.currentUser?.uid!!
                                val teacher = hashMapOf(
                                    "teacherName" to name,
                                    "teacherEmail" to email,
                                    "teacherMobile" to mobile,
                                    "teacherPassword" to password,
                                    "teacherStatus" to "pending",  // Default status
                                    "teacherUid" to uid
                                )
                                db.collection("teachers").document(uid)
                                    .set(teacher)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this,
                                            "Registered! Waiting for approval.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        auth.signOut()
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            this,
                                            "Error: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this, "Password and confirm password do not match!", Toast.LENGTH_SHORT).show()
            }
        }


    }
}