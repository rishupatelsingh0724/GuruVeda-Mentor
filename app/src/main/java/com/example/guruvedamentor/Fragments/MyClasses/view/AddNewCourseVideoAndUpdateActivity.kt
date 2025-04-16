package com.example.guruvedamentor.Fragments.MyClasses.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.guruvedamentor.databinding.ActivityAddNewCourseVideoBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddNewCourseVideoAndUpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddNewCourseVideoBinding
    private var videoUri: Uri? = null
    private lateinit var courseId: String

    private lateinit var videoTitle: String
    private lateinit var videoDescription: String
    private lateinit var videoType: String
    private lateinit var videoUrl: String
    private lateinit var videoId: String
    private var isUpdate: Boolean = false
    private lateinit var radioGroupType: RadioGroup
    private lateinit var radioFree: RadioButton
    private lateinit var radioPaid: RadioButton

    lateinit var oldVideoUrlFromIntent: String
    lateinit var oldDurationFromIntent: String

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNewCourseVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        courseId = intent.getStringExtra("courseId") ?: ""
        val selectVideo = binding.videoPreviewContainer
        selectVideo.setOnClickListener {
            checkPermission()
        }
        val saveVideo = binding.btnSaveVideo
        saveVideo.setOnClickListener {
            val title = binding.videoTitle.text.toString()
            val description = binding.videoDescription.text.toString()
            val type = if (binding.radioFree.isChecked) "Free" else "Paid"
            if (videoUri != null) {
                uploadVideoToFirebase(videoUri!!, courseId, title, description, type)
            } else {
                Toast.makeText(this, "Please select a video", Toast.LENGTH_SHORT).show()
            }
        }


        isUpdate = intent.getBooleanExtra("isUpdate", false)
        if (isUpdate) {
            videoTitle = intent.getStringExtra("videoTitle").toString()
            videoDescription = intent.getStringExtra("videoDescription").toString()
            videoType = intent.getStringExtra("videoType").toString()
            videoUrl = intent.getStringExtra("videoUrl").toString()
            videoId = intent.getStringExtra("videoId").toString()
            videoUri = Uri.parse(videoUrl)
            binding.videoView.setVideoURI(videoUri)
            binding.videoView.visibility = View.VISIBLE
        //    binding.videoView.setVideoURI(videoUri)
            binding.videoView.start()


            radioGroupType = binding.videoTypeGroup
            radioFree = binding.radioFree
            radioPaid = binding.radioPaid
            binding.videoTitle.setText(videoTitle)
            binding.videoDescription.setText(videoDescription)
            if (videoType == "Free") {
                radioFree.isChecked = true
            } else {
                radioPaid.isChecked = true
            }
            binding.btnSaveVideo.visibility = View.GONE
            binding.btnUpdateVideo.visibility = View.VISIBLE
        }else{
            binding.btnSaveVideo.visibility = View.VISIBLE
            binding.btnUpdateVideo.visibility = View.GONE
        }


        binding.btnUpdateVideo.setOnClickListener {
            if (videoUri != null && videoUri.toString() != videoUrl) {
                // Agar user ne naya video select kiya
                uploadUpdatedVideo(videoUri!!)
            } else {
                // Agar video same hai, sirf title ya description change hua hai
                updateVideoData(videoUrl)
            }

        }

        oldVideoUrlFromIntent = intent.getStringExtra("videoUrl").toString()
        oldDurationFromIntent = intent.getStringExtra("duration").toString()

    }

    @SuppressLint("IntentReset")
    fun selectVideo() {
        val cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "video/*"

        val chooser = Intent.createChooser(galleryIntent, "Select Video")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        startActivityForResult(chooser, 200)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermission() {
        val permissionsToRequest = arrayListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.RECORD_AUDIO)
        }

        if (permissionsToRequest.isEmpty()) {
            selectVideo()
        } else {
            requestPermissions(permissionsToRequest.toTypedArray(), 200)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == RESULT_OK) {
             videoUri = data?.data
            if (videoUri != null) {
                binding.videoView.setVideoURI(videoUri)
                binding.videoView.visibility = View.VISIBLE
                binding.videoView.setVideoURI(videoUri)
                binding.videoView.start()
            }
        }
    }

    fun saveVideoDataToFirestore(
        title: String,
        description: String,
        type: String,
        videoUrl: String,
        videoId: String,
        duration: String
    ) {
        val firestore = FirebaseFirestore.getInstance()

        val videoData = hashMapOf(
            "title" to title,
            "description" to description,
            "type" to type,
            "videoUrl" to videoUrl,
            "id" to videoId,
            "duration" to duration
        )

        firestore
            .collection("courses")
            .document(courseId)
            .collection("videos")
            .document(videoId)
            .set(videoData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Video added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                println("Error adding video: $e")
            }

    }

    fun uploadVideoToFirebase(videoUri: Uri, courseId: String, title: String, description: String, type: String){
        val videoId = System.currentTimeMillis().toString()
        val storageRef = FirebaseStorage.getInstance().reference.child("courses/$courseId/$videoId.mp4")

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading Video")
        progressDialog.setMessage("Please wait while the video is being uploaded")
        progressDialog.setCancelable(false)
        progressDialog.show()

        storageRef.putFile(videoUri)
            .addOnSuccessListener { taskSnapshot ->
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val rawDuration = getVideoDuration(this, videoUri)
                    val duration = formatDuration(rawDuration).toString()
                    saveVideoDataToFirestore(title, description, type, uri.toString(), videoId, duration)
                    progressDialog.dismiss()
                    finish()
                }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload video", Toast.LENGTH_SHORT).show()
            }
    }

    fun getVideoDuration(context: Context, uri: Uri): Long {
        val retriever = android.media.MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        val duration = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)
        retriever.release()
        return duration?.toLongOrNull() ?: 0L
    }

    @SuppressLint("DefaultLocale")
    fun formatDuration(durationMillis: Long): String {
        val totalSeconds = durationMillis / 1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val hours = totalSeconds / 3600

        return if (hours > 0)
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else
            String.format("%02d:%02d", minutes, seconds)
    }



// Update Video Functionality

    fun updateVideoData(videoUrl1: String) {

        val title = binding.videoTitle.text.toString()
        val description = binding.videoDescription.text.toString()
        val type = if (binding.radioFree.isChecked) "Free" else "Paid"

        val firestore = FirebaseFirestore.getInstance()
        val videoData = HashMap<String, Any>()
        videoData["title"] = title
        videoData["description"] = description
        videoData["type"] = type
        videoData["videoUrl"] = videoUrl1

        if (videoUrl1 != oldVideoUrlFromIntent){
            val rawDuration = getVideoDuration(this, videoUri!!)
            val duration = formatDuration(rawDuration).toString()
            videoData["duration"] = duration
        }else{
            videoData["duration"] = oldDurationFromIntent
        }
        

        firestore
            .collection("courses")
            .document(courseId)
            .collection("videos")
            .document(videoId).update(videoData)
            .addOnSuccessListener {
            Toast.makeText(this, "Video Updated Successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to update video", Toast.LENGTH_SHORT).show()

        }
    }



    fun uploadUpdatedVideo(newVideoUri: Uri) {

        val storageRef = FirebaseStorage.getInstance().reference.child("courses/$courseId/$videoId.mp4")

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Updating Video")
        progressDialog.setMessage("Uploading new video...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        storageRef.putFile(newVideoUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    progressDialog.dismiss()
                    // Ab naye URL ke saath update karenge
                    updateVideoData(uri.toString())
                }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Video upload failed", Toast.LENGTH_SHORT).show()
            }
    }



}