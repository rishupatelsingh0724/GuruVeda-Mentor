package com.example.guruvedamentor.Fragments.MyClasses.DataModel

data class CourseDataModel(
    val courseId: String? = null,
    val teacherId: String? = null,
    val courseTitle: String? = null,
    val videoCount: String? = null,
    val coursePrice: String? = null,
    val teacherName: String? = null,
    val courseDescription: String? = null,
    val courseThumbnail: String? = null
)

data class VideoDataModel(
    val id: String? = "",
    val title: String? = "",
    val description: String? = "",
    val videoUrl: String? = "",
    val type : String? = "",
    val duration : String? = ""
)