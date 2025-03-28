package com.example.guruvedamentor.Fragments.Profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.guruvedamentor.DataClass.ProfileDataModel
import com.example.guruvedamentor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {
    private lateinit var firebaseDB: FirebaseFirestore
    lateinit var tvTeacherName: TextView
    lateinit var tvTeacherEmail: TextView
    lateinit var tvTeacherPhone: TextView
    lateinit var ivTeacherImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvTeacherName = view.findViewById(R.id.teacherProfileName)
        tvTeacherEmail = view.findViewById(R.id.teacherProfileEmail)
        tvTeacherPhone = view.findViewById(R.id.teacherProfileNumber)
        ivTeacherImage = view.findViewById(R.id.teacherProfileImage)
        firebaseDB = FirebaseFirestore.getInstance()
        getTeacherProfileData()













        return view

    }

    private fun getTeacherProfileData() {
        val name = tvTeacherName
        val email = tvTeacherEmail
        val phone = tvTeacherPhone
        val image = ivTeacherImage

        val id = FirebaseAuth.getInstance().currentUser?.uid
        firebaseDB.collection("teachers").document(id!!).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {

                    val user = documentSnapshot.toObject(ProfileDataModel::class.java)
                    name.text = user?.teacherName
                    email.text = user?.teacherEmail
                    phone.text = user?.teacherMobile
                    Glide.with(this).load(user?.teacherImage).into(image)
                }
            }
    }

}