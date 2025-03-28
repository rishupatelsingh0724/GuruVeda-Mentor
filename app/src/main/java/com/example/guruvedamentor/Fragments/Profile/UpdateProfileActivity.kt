package com.example.guruvedamentor.Fragments.Profile

import android.os.Bundle
import android.provider.ContactsContract.Profile
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.guruvedamentor.DataClass.ProfileDataModel
import com.example.guruvedamentor.databinding.ActivityUpdateProfileBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var firebaseDB: FirebaseFirestore
    private lateinit var binding: ActivityUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseDB = FirebaseFirestore.getInstance()


    }

    fun getTeacherProfileData() {
        val teacherName = binding.etName
        val teacherEmail = binding.etEmail
        val teacherPhone = binding.etPhone
        val teacherImage = binding.ivProfile
        val id = FirebaseAuth.getInstance().currentUser?.uid
        firebaseDB.collection("teachers").document(id!!).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {

                    val user = documentSnapshot.toObject(ProfileDataModel::class.java)
                    teacherName.setText(user?.teacherName)
                    teacherEmail.setText(user?.teacherEmail)
                    teacherPhone.setText(user?.teacherMobile)
                    Glide.with(this).load(user?.teacherImage).into(teacherImage)
                }
            }
    }

}