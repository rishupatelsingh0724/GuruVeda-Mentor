package com.example.guruvedamentor.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.guruvedamentor.Adapters.FreeVideosAdapter
import com.example.guruvedamentor.Adapters.RecommendedCoursesAdapter
import com.example.guruvedamentor.DataModel.FreeVideosDataModel
import com.example.guruvedamentor.Fragments.MyClasses.DataModel.CourseDataModel
import com.example.guruvedamentor.MainActivity
import com.example.guruvedamentor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var recommendedCourses:RecyclerView
    lateinit var myCourses:CardView
    lateinit var manageTests:CardView
    lateinit var studyMaterial:CardView
    lateinit var liveBatches:CardView
    lateinit var recordedBatches:CardView
    lateinit var lectureVideos:CardView
    lateinit var freeVideos:RecyclerView
    lateinit var myCoursesAdapter: RecommendedCoursesAdapter
    lateinit var myCoursesList:ArrayList<CourseDataModel>
    lateinit var freeVideosList:ArrayList<FreeVideosDataModel>
    lateinit var freeVideosAdapter:FreeVideosAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_home, container, false)

        db= FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()
        myCoursesList= ArrayList()
        myCoursesAdapter= RecommendedCoursesAdapter(myCoursesList)
        freeVideosList= ArrayList()
        freeVideosAdapter= FreeVideosAdapter(freeVideosList)

        recommendedCourses=view.findViewById(R.id.recommendedRecyclerView)
        myCourses=view.findViewById(R.id.myCourses)
        manageTests=view.findViewById(R.id.manageTests)
        studyMaterial=view.findViewById(R.id.studyMaterial)
        liveBatches=view.findViewById(R.id.liveBatches)
        recordedBatches=view.findViewById(R.id.recordedBatches)
        lectureVideos=view.findViewById(R.id.lectureVideos)
        freeVideos=view.findViewById(R.id.freeVideosRecyclerView)



        recommendedCourses.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        recommendedCourses.adapter = myCoursesAdapter

        freeVideos.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        freeVideos.adapter=freeVideosAdapter


        myCourses.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("myCourses", "myCourses")
            startActivity(intent)
        }
        manageTests.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("manageTests", "manageTests")
            startActivity(intent)

        }
        studyMaterial.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("studyMaterial", "studyMaterial")
            startActivity(intent)
        }

        liveBatches.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("liveBatches", "liveBatches")
            startActivity(intent)
        }
        recordedBatches.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("recordedBatches", "recordedBatches")
            startActivity(intent)
        }
        lectureVideos.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("lectureVideos", "lectureVideos")
            startActivity(intent)
        }




        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider)

        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.offer_banner_1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.offer_banner_3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.offer_banner_2, ScaleTypes.FIT))
        imageSlider.setImageList(imageList)

        getRecommendedCourses()
        getFreeVideos()

        return view
    }
    @SuppressLint("NotifyDataSetChanged")
    fun getRecommendedCourses(){
        db.collection("courses")
            .get()
            .addOnSuccessListener {
                myCoursesList.clear()
                for (document in it.documents) {
                    val course = document.toObject(CourseDataModel::class.java)
                    myCoursesList.add(course!!)
                }
                myCoursesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getFreeVideos(){
        db.collection("videos").whereEqualTo("type","Free").get()
            .addOnSuccessListener {
                freeVideosList.clear()
                for (document in it.documents) {
                    val video = document.toObject(FreeVideosDataModel::class.java)
                    freeVideosList.add(video!!)
                }
                freeVideosAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }

    }



}