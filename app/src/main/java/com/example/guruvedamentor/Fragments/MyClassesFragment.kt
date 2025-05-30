package com.example.guruvedamentor.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.Fragments.MyClasses.Adapter.CoursesAdapter
import com.example.guruvedamentor.Fragments.MyClasses.DataModel.CourseDataModel
import com.example.guruvedamentor.R
import com.example.guruvedamentor.Fragments.MyClasses.view.AddCoursesActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore


class MyClassesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CoursesAdapter
    private lateinit var courseList: ArrayList<CourseDataModel>

    lateinit var FirestoreDB: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_classes, container, false)

//        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
//        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
//
//        val adapter = MyClassesPagerAdapter(this)
//        viewPager.adapter = adapter
//
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            when (position) {
//                0 -> tab.text = "Upcoming"
//                1 -> tab.text = "Live"
//                2 -> tab.text = "Free Videos"
//            }
//        }.attach()

        FirestoreDB = FirebaseFirestore.getInstance()
        courseList = ArrayList()

        recyclerView = view.findViewById(R.id.courseRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        courseAdapter = CoursesAdapter(requireContext(), courseList)
        recyclerView.adapter = courseAdapter

        var addCourses = view.findViewById<FloatingActionButton>(R.id.addCourses)
        addCourses.setOnClickListener {
            val intent = Intent(requireContext(), AddCoursesActivity::class.java)
            startActivity(intent)
        }








        fetchCourses()

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchCourses() {
        val firestoreDB = FirebaseFirestore.getInstance()

        firestoreDB.collection("courses")
            .get()
            .addOnSuccessListener { result ->
                courseList.clear()
                for (document in result) {
                    val course = document.toObject(CourseDataModel::class.java)
                    courseList.add(course)
                }
                courseAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch courses", Toast.LENGTH_SHORT).show()
            }
    }


}