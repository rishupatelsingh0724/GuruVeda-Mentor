package com.example.guruvedamentor.View

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.guruvedamentor.Fragments.MyClasses.DataModel.VideoDataModel
import com.example.guruvedamentor.R
import com.google.firebase.firestore.FirebaseFirestore

class VideoDetailsActivity : AppCompatActivity() {
    lateinit var playerView: PlayerView
    lateinit var videoTitle: TextView
    lateinit var videoDescription: TextView
    lateinit var db: FirebaseFirestore
    lateinit var courseId: String
    private lateinit var player: ExoPlayer

    lateinit var videoList: ArrayList<VideoDataModel>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_video_details)

        videoList = ArrayList()

        playerView = findViewById(R.id.playerView)
        videoTitle = findViewById<TextView>(R.id.videoTitle)
        videoDescription = findViewById<TextView>(R.id.videDescription)
        db = FirebaseFirestore.getInstance()
        courseId = intent.getStringExtra("courseId").toString()


        getVideo()


    }

    @SuppressLint("UseKtx")
    fun getVideo() {
        db.collection("courses").document(courseId).collection("videos").get()
            .addOnSuccessListener { result ->
                for (dataSnapshot in result) {
                    val video = dataSnapshot.toObject(VideoDataModel::class.java)
                    videoList.add(video)

                }

                if (videoList.isNotEmpty()) {
                    val video = videoList[0]
                    val videoUri = Uri.parse(video.videoUrl)

                    videoTitle.text = video.title
                    videoDescription.text = video.description

                    player = ExoPlayer.Builder(this).build()
                    playerView.player = player

                    val mediaItem = MediaItem.fromUri(videoUri)
                    player.setMediaItem(mediaItem)
                    player.prepare()
                    player.playWhenReady = true
                }
                playerView.requestFocus()

            }


    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}
