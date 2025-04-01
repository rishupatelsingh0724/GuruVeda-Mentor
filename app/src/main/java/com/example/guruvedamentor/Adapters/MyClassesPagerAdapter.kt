package com.example.guruvedamentor.Adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.guruvedamentor.Fragments.MyClasses.FreeVideosFragment
import com.example.guruvedamentor.Fragments.MyClasses.LiveFragment
import com.example.guruvedamentor.Fragments.MyClasses.UpcomingFragment

class MyClassesPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingFragment()
            1 -> LiveFragment()
            2 -> FreeVideosFragment()
            else -> UpcomingFragment()
        }
    }
}