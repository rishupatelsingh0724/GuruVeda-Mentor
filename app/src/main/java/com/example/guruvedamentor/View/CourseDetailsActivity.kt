package com.example.guruvedamentor.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.guruvedamentor.R

class CourseDetailsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_course_detials)

        val courseImageView = findViewById<ImageView>(R.id.courseImageView)
        val courseTitleTV = findViewById<TextView>(R.id.courseTitleTV)
        val courseDescriptionTV = findViewById<TextView>(R.id.courseDescriptionTV)
        val coursePriceTV = findViewById<TextView>(R.id.coursePriceTV)
        val videoImageView = findViewById<ImageView>(R.id.videoImageView)

        val courseImage = intent.getStringExtra("courseThumbnail")
        val courseTitle = intent.getStringExtra("courseTitle")
        val courseDescription = intent.getStringExtra("courseDescription")
        val coursePrice = intent.getStringExtra("coursePrice")
        val courseId = intent.getStringExtra("courseId")



        Glide.with(this).load(courseImage).into(courseImageView)
        courseTitleTV.text = courseTitle
        courseDescriptionTV.text = courseDescription
        coursePriceTV.text = coursePrice

        videoImageView.setOnClickListener {
            val intent = Intent(this, VideoDetailsActivity::class.java)
            intent.putExtra("courseId", courseId)
            startActivity(intent)

        }


    }
}