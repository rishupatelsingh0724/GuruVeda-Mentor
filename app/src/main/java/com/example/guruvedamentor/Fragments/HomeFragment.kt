package com.example.guruvedamentor.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.guruvedamentor.R


class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_home, container, false)






        
        
        
        
        
        
        



        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider)

        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.offer_banner_1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.offer_banner_3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.offer_banner_2, ScaleTypes.FIT))
        imageSlider.setImageList(imageList)


        return view
    }

}