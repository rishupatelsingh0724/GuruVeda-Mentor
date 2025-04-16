package com.example.guruvedamentor.Fragments.Profile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.guruvedamentor.databinding.ActivityUpdateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.core.net.toUri

class UpdateProfileActivity : AppCompatActivity() {
    private var imageUri: Uri? = null
    private lateinit var firebaseDB: FirebaseFirestore
    private lateinit var binding: ActivityUpdateProfileBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseDB = FirebaseFirestore.getInstance()

        val teacherName = intent.getStringExtra("teacherName")
        val teacherEmail = intent.getStringExtra("teacherEmail")
        val teacherPhone = intent.getStringExtra("teacherPhone")
        val teacherImage = intent.getStringExtra("teacherImage")
        Glide.with(this).load(teacherImage).into(binding.ivProfile)
        binding.etName.setText(teacherName)
        binding.etEmail.setText(teacherEmail)
        binding.etPhone.setText(teacherPhone)
        binding.etPhone.setOnClickListener{ Toast.makeText(this, "Sorry! you can't be change phone number ☹️", Toast.LENGTH_SHORT).show()}
        binding.etEmail.setOnClickListener{ Toast.makeText(this, "Sorry! you can't be change email address ☹️", Toast.LENGTH_SHORT).show()}
        binding.etName.setOnClickListener{ Toast.makeText(this, "Sorry! you can't be change name ☹️", Toast.LENGTH_SHORT).show()}

        binding.ivWrong.setOnClickListener {
            finish()
        }
        binding.ivProfile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                selectImage()
            } else {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    ), 100
                )
            }
        }
        binding.btnSave.setOnClickListener {
            updateTeacherProfileImage()
            finish()
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
                binding.ivProfile.setImageURI(data?.data)
            } else if (data?.extras?.get("data") != null) {
                val bitmap = data.extras?.get("data") as Bitmap
                imageUri = getImageUriFromBitmap(bitmap)
                binding.ivProfile.setImageBitmap(imageUri as Bitmap?)
            }else{
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateTeacherProfileImage(){
        val id = FirebaseAuth.getInstance().currentUser?.uid?:return
        if (imageUri == null){
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }
        firebaseDB.collection("teachers").document(id).update("teacherImage", imageUri.toString())
            .addOnSuccessListener {
                Toast.makeText(this, "Profile Image Updated Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to Update Profile Image", Toast.LENGTH_SHORT).show()
            }
    }
    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "ProfileImage", null)
        return path.toUri()
    }
}