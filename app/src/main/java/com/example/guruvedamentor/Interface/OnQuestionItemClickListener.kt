package com.example.guruvedamentor.Interface

import com.example.guruvedamentor.DataClass.QuestionDataModel

interface OnQuestionItemClickListener {
    fun onUpdateClicked(position: Int, question: QuestionDataModel)
    fun onDeleteClicked(position: Int, question: QuestionDataModel)
}