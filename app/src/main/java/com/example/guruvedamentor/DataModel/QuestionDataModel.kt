package com.example.guruvedamentor.DataModel

data class QuestionDataModel(
    val teacherId: String?=null,
    var questionId: String? = null,
    var question: String?=null,
    var optionA: String?=null,
    var optionB: String?=null,
    var optionC: String?=null,
    var optionD: String?=null,
    var correctAnswer: String?=null
)
