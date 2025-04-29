package com.example.guruvedamentor.Fragments.MyClasses.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guruvedamentor.Fragments.MyClasses.Adapter.AddCourseVideoAdapter
import com.example.guruvedamentor.Fragments.MyClasses.DataModel.VideoDataModel
import com.example.guruvedamentor.databinding.ActivityShowCourseDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ShowCourseDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowCourseDetailsBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: AddCourseVideoAdapter
    lateinit var videoList: MutableList<VideoDataModel>
    private lateinit var courseId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShowCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        courseId = intent.getStringExtra("courseId").toString()
        videoList = mutableListOf()
        recyclerView = binding.videoRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AddCourseVideoAdapter(this, videoList, courseId,{ video ->
            openVideoInPlayer(video)
        })
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter


        val courseTitle = intent.getStringExtra("courseTitle")
        val courseDescription = intent.getStringExtra("courseDescription")
        val courseThumbnail = intent.getStringExtra("courseThumbnail")
        binding.courseTitle.text = courseTitle
        binding.courseDescription.text = courseDescription
        Glide.with(this).load(courseThumbnail).into(binding.courseBanner)

        binding.btnAddNewCourseVideo.setOnClickListener {
            val intent = Intent(this, AddNewCourseVideoAndUpdateActivity::class.java)
            intent.putExtra("courseId", courseId)
            startActivity(intent)
        }
        binding.btnPlayAll.setOnClickListener {
            val intent = Intent(this, CourseVideoPlaygroundActivity::class.java).apply {
                putExtra("courseId", courseId)
                putExtra("videoUrl", videoList.first().videoUrl)
                putExtra("videoTitle", videoList.first().title)
                putExtra("videoDescription", videoList.first().description)
                putExtra("videoDuration", videoList.first().duration)
                putExtra("videoType", videoList.first().type)
            }
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        fetchCourseVideos(courseId)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun fetchCourseVideos(courseId: String){
        val db = FirebaseFirestore.getInstance()
        val videoCollection = db.collection("videos")
            .whereEqualTo("courseId", courseId)
            .orderBy("id",
            Query.Direction.ASCENDING).limit(10)
        videoCollection.get().addOnSuccessListener { documents ->
            videoList.clear()
            for (document in documents) {
                val video = document.toObject(VideoDataModel::class.java)
                videoList.add(video)
            }
            adapter.notifyDataSetChanged()
        }
    }

    fun openVideoInPlayer(video: VideoDataModel) {
        val intent = Intent(this, CourseVideoPlaygroundActivity::class.java).apply {
            putExtra("courseId", courseId)
            putExtra("videoUrl", video.videoUrl)
            putExtra("videoTitle", video.title)
            putExtra("videoDescription", video.description)
            putExtra("videoDuration", video.duration)
            putExtra("videoType", video.type)
        }
        startActivity(intent)
    }
}