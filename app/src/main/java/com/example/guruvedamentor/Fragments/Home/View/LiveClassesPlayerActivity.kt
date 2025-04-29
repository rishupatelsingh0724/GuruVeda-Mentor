package com.example.guruvedamentor.Fragments.Home.View


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.guruvedamentor.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeScopes
import com.google.api.services.youtube.model.CdnSettings
import com.google.api.services.youtube.model.LiveBroadcast
import com.google.api.services.youtube.model.LiveBroadcastSnippet
import com.google.api.services.youtube.model.LiveBroadcastStatus
import com.google.api.services.youtube.model.LiveStream
import com.google.api.services.youtube.model.LiveStreamSnippet

class LiveClassesPlayerActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 101
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var credential: GoogleAccountCredential
    private lateinit var youtubeService: YouTube

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_live_classes_player)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(YouTubeScopes.YOUTUBE), Scope(YouTubeScopes.YOUTUBE_FORCE_SSL))
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        signInUser()
    }
    private fun signInUser() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                if (task.isSuccessful) {
                    val account = task.result
                    Toast.makeText(this, "Sign-in Successful!", Toast.LENGTH_SHORT).show()
                    setupYouTubeService(account)
                } else {
                    Toast.makeText(this, "Sign-in Failed!", Toast.LENGTH_SHORT).show()
                    Log.e("SignInError", task.exception?.message ?: "Unknown error")
                }
            } else {
                Toast.makeText(this, "Sign-in Cancelled by User!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupYouTubeService(account: GoogleSignInAccount) {
        try {
            credential = GoogleAccountCredential.usingOAuth2(
                applicationContext,
                listOf(YouTubeScopes.YOUTUBE, YouTubeScopes.YOUTUBE_FORCE_SSL)
            )
            credential.selectedAccount = account.account

            youtubeService = YouTube.Builder(
                NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
            )
                .setApplicationName("YouTubeLiveStreamApp")
                .build()

            Toast.makeText(this, "YouTube Service Setup Done!", Toast.LENGTH_SHORT).show()

            createLiveStream()

        } catch (e: Exception) {
            Toast.makeText(this, "YouTube Service Setup Failed!", Toast.LENGTH_SHORT).show()
            Log.e("YouTubeSetupError", e.message ?: "Unknown error")
        }
    }

    private fun createLiveStream() {
        Thread {
            try {
                val broadcast = LiveBroadcast()
                    .setSnippet(
                        LiveBroadcastSnippet().apply {
                            title = "Test Live Stream"
                            scheduledStartTime = DateTime("2025-04-25T10:00:00.000Z")
                        }
                    )
                    .setStatus(LiveBroadcastStatus().apply {
                        privacyStatus = "public"
                    })
                    .setKind("youtube#liveBroadcast")

                val insertedBroadcast = youtubeService.liveBroadcasts()
                    .insert("snippet,status", broadcast)
                    .execute()

                val stream = LiveStream()
                    .setSnippet(
                        LiveStreamSnippet().apply {
                            title = "Test Stream"
                        }
                    )
                    .setCdn(
                        CdnSettings().apply {
                            format = "1080p"
                            ingestionType = "rtmp"
                        }
                    )
                    .setKind("youtube#liveStream")

                val insertedStream = youtubeService.liveStreams()
                    .insert("snippet,cdn", stream)
                    .execute()

                // Bind the stream to the broadcast
                youtubeService.liveBroadcasts()
                    .bind(insertedBroadcast.id, "id,contentDetails")
                    .setStreamId(insertedStream.id)
                    .execute()

                val ingestionUrl = insertedStream.cdn.ingestionInfo.ingestionAddress
                val streamName = insertedStream.cdn.ingestionInfo.streamName

                runOnUiThread {
                    Toast.makeText(this, "Live Stream Created!\nRTMP URL: $ingestionUrl/$streamName", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error Creating Live Stream!", Toast.LENGTH_SHORT).show()
                }
                Log.e("LiveStreamError", e.message ?: "Unknown error")
            }
        }.start()
    }

}