package com.example.guruvedamentor.Fragments.MyClasses.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.Fragments.MyClasses.Adapter.CourseVideoPlaygroundAdapter
import com.example.guruvedamentor.Fragments.MyClasses.DataModel.VideoDataModel
import com.example.guruvedamentor.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CourseVideoPlaygroundActivity : AppCompatActivity() {
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CourseVideoPlaygroundAdapter
    lateinit var videoList: MutableList<VideoDataModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_course_video_playground)

        playerView = findViewById(R.id.courseVideoPlayerView)
        player = ExoPlayer.Builder(this).build()
        playerView.player = player


        val videoUrl = intent.getStringExtra("videoUrl")
        val videoTitle = intent.getStringExtra("videoTitle")
        val videoDescription = intent.getStringExtra("videoDescription")
        val videoDuration = intent.getStringExtra("videoDuration")
        val videoType = intent.getStringExtra("videoType")

        findViewById<TextView>(R.id.videoTitleTextView).text = videoTitle
        findViewById<TextView>(R.id.videoDescriptionTextView).text = videoDescription

        playVideo(videoUrl.toString())

        recyclerView = findViewById(R.id.playlistRecyclerView)
        videoList = mutableListOf()
        adapter = CourseVideoPlaygroundAdapter(this, videoList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Views ko find karo
        val videoContainer = findViewById<PlayerView>(R.id.courseVideoPlayerView)
        val playlist = findViewById<RecyclerView>(R.id.playlistRecyclerView)
        val title = findViewById<TextView>(R.id.videoTitleTextView)
        val desc = findViewById<TextView>(R.id.videoDescriptionTextView)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 🔄 LANDSCAPE: Fullscreen video
            supportActionBar?.hide()
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )

            videoContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            title.visibility = View.GONE
            desc.visibility = View.GONE
            playlist.visibility = View.GONE

        } else {
            // 🔄 PORTRAIT: Normal mode
            supportActionBar?.show()
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

            // portrait mode me height fix kar do
            videoContainer.layoutParams.height =
                resources.getDimensionPixelSize(R.dimen.video_height_portrait)

            title.visibility = View.VISIBLE
            desc.visibility = View.VISIBLE
            playlist.visibility = View.VISIBLE
        }
    }


    fun playVideo(videoUrl: String) {
        val mediaItem = MediaItem.fromUri(videoUrl) // videoUrl -> MP4 link
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
    override fun onResume() {
        super.onResume()
        fetchCourseVideos(intent.getStringExtra("courseId").toString())
    }

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

}