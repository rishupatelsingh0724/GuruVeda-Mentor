package com.example.guruvedamentor

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.guruvedamentor.Fragments.HomeFragment
import com.example.guruvedamentor.Fragments.ManageTestsFragment
import com.example.guruvedamentor.Fragments.MyClassesFragment
import com.example.guruvedamentor.Fragments.Profile.ProfileFragment
import com.example.guruvedamentor.Fragments.ResourcesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        if (intent.getStringExtra("myCourses") == "myCourses") {
            bottomNavigationView.selectedItemId = R.id.my_classes
            loadFragment(MyClassesFragment())
        } else if (intent.getStringExtra("manageTests") == "manageTests") {
            bottomNavigationView.selectedItemId = R.id.manage_tests
            loadFragment(ManageTestsFragment())
        }
        else if (intent.getStringExtra("studyMaterial") == "studyMaterial") {
            bottomNavigationView.selectedItemId = R.id.resources
            loadFragment(ResourcesFragment())
        }
        else if (intent.getStringExtra("liveBatches") == "liveBatches") {
            bottomNavigationView.selectedItemId = R.id.resources
            loadFragment(ResourcesFragment())
        }

        else if (intent.getStringExtra("recordedBatches") == "recordedBatches") {
            bottomNavigationView.selectedItemId = R.id.resources
            loadFragment(ResourcesFragment())
        }

        else if (intent.getStringExtra("lectureVideos") == "lectureVideos") {
            bottomNavigationView.selectedItemId = R.id.resources
            loadFragment(ResourcesFragment())
        }
        else {
            loadFragment(HomeFragment())
        }



        bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }

                R.id.my_classes -> {
                    loadFragment(MyClassesFragment())
                    return@setOnItemSelectedListener true
                }

                R.id.manage_tests -> {
                    loadFragment(ManageTestsFragment())
                    return@setOnItemSelectedListener true
                }

                R.id.resources -> {
                    loadFragment(ResourcesFragment())
                    return@setOnItemSelectedListener true
                }

                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    return@setOnItemSelectedListener true
                }

                else -> {
                    false
                }
            }
        }

        statusBar()

    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutContainer,fragment)
        transaction.commit()
    }

    fun statusBar(){
        val statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.statusBarColor = statusBarColor

    }

}