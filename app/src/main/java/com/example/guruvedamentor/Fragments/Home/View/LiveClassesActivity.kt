package com.example.guruvedamentor.Fragments.Home.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guruvedamentor.R
import com.google.android.gms.common.SignInButton

class LiveClassesActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_live_classes)

        findViewById<Button>(R.id.live).setOnClickListener {
            startActivity(Intent(this, LiveClassesPlayerActivity::class.java))
        }

    }
}