package com.example.guruvedamentor.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.guruvedamentor.Auth.LoginActivity
import com.example.guruvedamentor.MainActivity
import com.example.guruvedamentor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        db = FirebaseFirestore.getInstance()
        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            checkUserStatus()
        }, 1000)
    }

    private fun checkUserStatus() {
        val currentUser = auth.currentUser
        if (currentUser == null) {

            goToLogin()
        } else {
            val uid = currentUser.uid
            db!!.collection("teachers").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val status = document.getString("teacherStatus")
                        if (status == "approved") {
                            goToMain()
                        } else {
                            Toast.makeText(
                                this,
                                "Approval Pending. Please wait!",
                                Toast.LENGTH_LONG
                            ).show()
                            goToLogin()
                        }
                    } else {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show()
                        goToLogin()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error fetching status!", Toast.LENGTH_SHORT).show()
                    goToLogin()
                }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}