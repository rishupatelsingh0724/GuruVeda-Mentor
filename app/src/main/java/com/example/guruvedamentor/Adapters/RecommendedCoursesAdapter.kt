package com.example.guruvedamentor.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guruvedamentor.Fragments.MyClasses.DataModel.CourseDataModel
import com.example.guruvedamentor.R
import com.example.guruvedamentor.View.CourseDetailsActivity

class RecommendedCoursesAdapter(var list: ArrayList<CourseDataModel>):RecyclerView.Adapter<RecommendedCoursesAdapter.CourseViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseViewHolder {
        val layout=LayoutInflater.from(parent.context).inflate(R.layout.recommended_courses_video_layout,parent,false)
        return CourseViewHolder(layout)
    }

    override fun onBindViewHolder(
        holder: CourseViewHolder,
        position: Int
    ) {
        val item=list[position]
        Glide.with(holder.itemView.context).load(item.courseThumbnail).placeholder(R.drawable.profile_icon).into(holder.courseImage)
        holder.courseTitle.text=item.courseTitle
        holder.courseDescription.text=item.courseDescription
        holder.coursePrice.text=item.coursePrice
        holder.teacherName.text=item.teacherName

        holder.itemView.setOnClickListener {
            val intent=Intent(holder.itemView.context, CourseDetailsActivity::class.java)
            intent.putExtra("courseThumbnail",item.courseThumbnail)
            intent.putExtra("courseTitle",item.courseTitle)
            intent.putExtra("courseDescription",item.courseDescription)
            intent.putExtra("coursePrice",item.coursePrice)
            intent.putExtra("teacherName",item.teacherName)
            intent.putExtra("courseId",item.courseId)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CourseViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val courseImage=view.findViewById<ImageView>(R.id.recommendedImageView)
        val courseTitle=view.findViewById<TextView>(R.id.courseTitle)
        val courseDescription=view.findViewById<TextView>(R.id.courseDescription)
        val coursePrice=view.findViewById<TextView>(R.id.coursePrice)
        val teacherName=view.findViewById<TextView>(R.id.teacherName)


    }
}