package com.example.guruvedamentor.View

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.guruvedamentor.R

class FreeVideosPlayerActivity : AppCompatActivity() {
    lateinit var playerView: PlayerView
    lateinit var videoTitle: TextView
    lateinit var videoDescription: TextView
    private lateinit var player: ExoPlayer

    @SuppressLint("MissingInflatedId", "UseKtx")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_free_videos_player)

        val videoUrl = intent.getStringExtra("videoUrl")
        val videosTitle = intent.getStringExtra("videoTitle")
        val videosDescription = intent.getStringExtra("videoDescription")

        playerView = findViewById(R.id.freeVideoPlayerView)
        videoTitle = findViewById(R.id.freeVideoTitle)
        videoDescription = findViewById(R.id.freeVideoDescription)

        val videoUri = Uri.parse(videoUrl)

        videoTitle.text = videosTitle
        videoDescription.text = videosDescription

        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val mediaItem = MediaItem.fromUri(videoUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
        playerView.requestFocus()


        handleOrientation(resources.configuration.orientation)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        handleOrientation(newConfig.orientation)
    }

    private fun handleOrientation(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            supportActionBar?.hide()
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )

            playerView.layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
            videoTitle.visibility = View.GONE
            videoDescription.visibility = View.GONE
        } else {

            supportActionBar?.show()
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            playerView.layoutParams.height = resources.getDimensionPixelSize(R.dimen.player_default_height)
            videoTitle.visibility = View.VISIBLE
            videoDescription.visibility = View.VISIBLE
        }
        playerView.requestLayout()
    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}