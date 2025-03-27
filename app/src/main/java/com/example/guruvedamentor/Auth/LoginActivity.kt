package com.example.guruvedamentor.Auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.guruvedamentor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    var dbEmail: String? = null
    var dbPassword: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        val btnLogin = findViewById<AppCompatButton>(R.id.btnLogin)
        val email = findViewById<EditText>(R.id.etEmailLogin)
        val password = findViewById<EditText>(R.id.etPasswordLogin)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        val tvSignupNow = findViewById<TextView>(R.id.tvSignupNow)

        tvForgotPassword.setOnClickListener {

        }

        tvSignupNow.setOnClickListener {
            startActivity(Intent(this, RequestRegisterActivity::class.java))
        }

        db.collection("teachers").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener { result ->
                if (result.exists()) {
                    dbEmail = result.getString("teacherEmail")
                    dbPassword = result.getString("teacherPassword")
                }
            }





        btnLogin.setOnClickListener {
            val etEmail=email.text.toString()
            val etPassword=password.text.toString()
            if (dbEmail == etEmail && dbPassword == etPassword) {
                if (etEmail.isNotEmpty() && etPassword.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(etEmail, etPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val uid = auth.currentUser?.uid!!
                                checkApprovalStatus(uid)
                            } else {
                                Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Enter email and password!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun checkApprovalStatus(uid: String) {
        db.collection("teachers").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val status = document.getString("status")
                    if (status == "approved") {
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Approval Pending. Please wait!", Toast.LENGTH_LONG)
                            .show()
                        auth.signOut()
                    }
                } else {
                    Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching status!", Toast.LENGTH_SHORT).show()
            }
    }

}