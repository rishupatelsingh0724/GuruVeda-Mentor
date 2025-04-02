package com.example.guruvedamentor.Adapters

import android.annotation.SuppressLint
import android.content.Context
import com.example.guruvedamentor.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guruvedamentor.DataModels.CourseDataModel


class CoursesAdapter(private val context: Context,private val courseList: List<CourseDataModel>): RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.my_courses_adapter_desing, parent, false)
        return CourseViewHolder(view)
    }

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseTitle: TextView = itemView.findViewById(R.id.courseTitle)
        val videoCount: TextView = itemView.findViewById(R.id.videoCount)
        val coursePrice: TextView = itemView.findViewById(R.id.coursePrice)
        val courseDescription: TextView = itemView.findViewById(R.id.courseDescription)
        val courseThumbnail: ImageView = itemView.findViewById(R.id.courseThumbnail)
        val courseArrow: ImageView = itemView.findViewById(R.id.courseArrow)
    }
    override fun getItemCount(): Int {
        return courseList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]

        // Setting data into views
        holder.courseTitle.text = course.courseTitle
        holder.videoCount.text = "${course.videoCount} Videos"
        holder.coursePrice.text = course.coursePrice
        holder.courseDescription.text = course.courseDescription

        Glide.with(context)
            .load(course.courseThumbnail)
            .into(holder.courseThumbnail)

        holder.itemView.setOnClickListener {
            // Open course details or whatever action needed
            Toast.makeText(holder.itemView.context, "Clicked on ${course.courseTitle}", Toast.LENGTH_SHORT).show()
        }
    }

}