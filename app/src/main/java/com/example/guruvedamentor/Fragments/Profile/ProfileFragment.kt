package com.example.guruvedamentor.Fragments.Profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.guruvedamentor.DataModel.ProfileDataModel
import com.example.guruvedamentor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {
    private lateinit var firebaseDB: FirebaseFirestore
    lateinit var tvTeacherName: TextView
    lateinit var tvTeacherEmail: TextView
    lateinit var tvTeacherPhone: TextView
    lateinit var ivTeacherImage: ImageView
    private var isDataLoaded = false
    private var isFetching = false
    private lateinit var teacherProfileDataList:ArrayList<ProfileDataModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isDataLoaded && !isFetching) {
            getTeacherProfileData()
        }
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
        teacherProfileDataList = ArrayList()



        val editButton = view.findViewById<Button>(R.id.btnEdit)
        editButton.setOnClickListener {
            val intent = Intent(requireContext(), UpdateProfileActivity::class.java)
            intent.putExtra("teacherName", tvTeacherName.text.toString())
            intent.putExtra("teacherEmail", tvTeacherEmail.text.toString())
            intent.putExtra("teacherPhone", tvTeacherPhone.text.toString())
            if (teacherProfileDataList != null && !teacherProfileDataList.isEmpty()) {
                intent.putExtra("teacherImage", teacherProfileDataList.get(0).teacherImage);
            } else {
                Log.e("Error", "teacherProfileDataList is empty!");
            }

            startActivity(intent)
        }











        return view

    }

    private fun getTeacherProfileData() {
        if (isFetching)return
        isFetching = true
        val id = FirebaseAuth.getInstance().currentUser?.uid
        if (id == null) {
            isFetching = false
            return
        }

        val name = tvTeacherName
        val email = tvTeacherEmail
        val phone = tvTeacherPhone
        val image = ivTeacherImage

        firebaseDB.collection("teachers").document(id!!).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(ProfileDataModel::class.java)
                    teacherProfileDataList.clear()
                    teacherProfileDataList.add(user!!)
                    name.text = user.teacherName
                    email.text = user.teacherEmail
                    phone.text = user.teacherMobile
                    if (user.teacherImage != null && isAdded && activity !=null) {
                        Glide.with(this).load(user.teacherImage).into(image)
                    } else {
                        image.setImageResource(R.drawable.profile_icon)
                    }
                    isDataLoaded= true
                }
            }.addOnFailureListener { e ->
                Log.e("ProfileFragment", "Error fetching profile: ${e.message}")
            }
            .addOnCompleteListener {  // ✅ Request complete hone ke baad flag reset karna zaroori hai
                isFetching = false
            }
    }

}