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
import com.example.guruvedamentor.Adapters.CoursesAdapter
import com.example.guruvedamentor.DataModels.CourseDataModel
import com.example.guruvedamentor.R
import com.example.guruvedamentor.View.AddCoursesActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MyClassesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CoursesAdapter
    private lateinit var courseList: ArrayList<CourseDataModel>

    lateinit var realtimeDB: FirebaseDatabase

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

        realtimeDB = FirebaseDatabase.getInstance()
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

    fun fetchCourses(){
        realtimeDB.reference.child("courses").addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                courseList.clear()
                for (data in snapshot.children){
                    val course = data.getValue(CourseDataModel::class.java)
                  if (course!=null){
                      courseList.add(course)

                  }
                }
                courseAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

}