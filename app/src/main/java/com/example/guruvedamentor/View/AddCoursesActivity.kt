package com.example.guruvedamentor.View

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.guruvedamentor.databinding.ActivityAddCoursesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddCoursesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCoursesBinding
    private var imageUri: Uri? = null
    lateinit var firebaseDB: FirebaseStorage
    lateinit var realtimeDB: FirebaseDatabase
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDB = FirebaseStorage.getInstance()
        realtimeDB = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val chooseImage = binding.btnChooseImage
        chooseImage.setOnClickListener {
            selectImage()
        }
        val saveCourse = binding.btnSaveCourse
        saveCourse.setOnClickListener {
            if (imageUri != null) {
                uploadImage(imageUri!!)
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }
    @SuppressLint("IntentReset")
    fun selectImage() {
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        gallery.type = "image/*"
        val chooser = Intent.createChooser(gallery, "Select Image")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(camera))
        startActivityForResult(chooser, 100)
    }
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK){
            imageUri = data?.data
            if (imageUri !=null) {
                binding.imageCourseThumbnail.setImageURI(data?.data)
            } else if (data?.extras?.get("data") != null) {
                val bitmap = data.extras?.get("data") as Bitmap
                imageUri = getImageUriFromBitmap(bitmap)
                binding.imageCourseThumbnail.setImageBitmap(imageUri as Bitmap?)
            }else{
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "ProfileImage", null)
        return path.toUri()
    }
    @SuppressLint("SuspiciousIndentation")
    private fun saveCourse(uri: String) {
        val courseTitle = binding.editCourseTitle.text.toString()
        val courseDescription = binding.editCourseDescription.text.toString()
        val videoCount = binding.editVideoCount.text.toString()
        val coursePrice = binding.editCoursePrice.text.toString()
        val courseId = UUID.randomUUID().toString()
        val teacherId = firebaseAuth.currentUser?.uid ?: ""
        var teacherName =String()
            FirebaseFirestore.getInstance().collection("teachers").document(teacherId).get().addOnSuccessListener {
            if (it.exists()) {
                teacherName = it.getString("teacherName") ?: ""
            }
            else{
                Toast.makeText(this, "Failed to get teacher name", Toast.LENGTH_SHORT).show()
            }

        }

        val data = hashMapOf(
            "courseId" to courseId,
            "teacherId" to teacherId,
            "teacherName" to teacherName,
            "courseTitle" to courseTitle,
            "courseDescription" to courseDescription,
            "videoCount" to videoCount,
            "coursePrice" to coursePrice,
            "courseThumbnail" to uri
        )
        realtimeDB.getReference("courses").child(courseId).setValue(data).addOnSuccessListener {
            Toast.makeText(this, "Course Saved Successfully", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to Save Course", Toast.LENGTH_SHORT).show()
        }

    }
    private fun uploadImage(uri: Uri) {
        val storageRef = firebaseDB.reference.child("courses/${UUID.randomUUID()}.jpg")
        storageRef.putFile(uri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                saveCourse(uri.toString())
            }
        }
    }
}