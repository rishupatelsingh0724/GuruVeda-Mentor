package com.example.guruvedamentor.View

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.Adapters.GetQuestionAdapter
import com.example.guruvedamentor.DataClass.QuestionDataModel
import com.example.guruvedamentor.Interface.OnQuestionItemClickListener
import com.example.guruvedamentor.R
import com.google.firebase.firestore.FirebaseFirestore

class QuestionsActivity : AppCompatActivity(), OnQuestionItemClickListener {
    @SuppressLint("MissingInflatedId")
    lateinit var questionRecyclerView: RecyclerView
    lateinit var questionList: ArrayList<QuestionDataModel>
    lateinit var questionAdapter: GetQuestionAdapter
    lateinit var dp: FirebaseFirestore
    lateinit var menuImageView: ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_questions)

        dp= FirebaseFirestore.getInstance()

        questionList=ArrayList()
        questionAdapter= GetQuestionAdapter(questionList,this)

        val subject=findViewById<TextView>(R.id.textViewSubject)
        questionRecyclerView=findViewById(R.id.questions_recyclerView)
        subject.text=intent.getStringExtra("subject")

        questionRecyclerView.layoutManager=LinearLayoutManager(this)
        questionRecyclerView.adapter=questionAdapter
        getQuestions()


    }
    fun getQuestions(){
        dp.collection("teacher_tests_question").get().addOnSuccessListener {
            questionList.clear()
            for (dataSnapshot in it) {
                val question = dataSnapshot.toObject(QuestionDataModel::class.java)
                questionList.add(question)
                questionAdapter.notifyDataSetChanged()
            }

        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onUpdateClicked(
        position: Int,
        question: QuestionDataModel
    ) {

        val alertDialog=AlertDialog.Builder(this)
        alertDialog.setTitle("Update Question")
        val view= LayoutInflater.from(this).inflate(R.layout.update_question_layout,null,false)
        val editTextQuestion=view.findViewById<TextView>(R.id.editTextQuestionUpdate)
        val editTextOptionA=view.findViewById<TextView>(R.id.editTextOptionAUpdate)
        val editTextOptionB=view.findViewById<TextView>(R.id.editTextOptionBUpdate)
        val editTextOptionC=view.findViewById<TextView>(R.id.editTextOptionCUpdate)
        val editTextOptionD=view.findViewById<TextView>(R.id.editTextOptionDUpdate)
        val editTextCorrectAnswer=view.findViewById<TextView>(R.id.editTextCorrectAnswerUpdate)

        editTextQuestion.text=question.question
        editTextOptionA.text=question.optionA
        editTextOptionB.text=question.optionB
        editTextOptionC.text=question.optionC
        editTextOptionD.text=question.optionD
        editTextCorrectAnswer.text=question.correctAnswer

        alertDialog.setPositiveButton("Update") { _, _ ->
            val updateQuestion = editTextQuestion.text.toString()
            val updateOptionA = editTextOptionA.text.toString()
            val updateOptionB = editTextOptionB.text.toString()
            val updateOptionC = editTextOptionC.text.toString()
            val updateOptionD = editTextOptionD.text.toString()
            val updateCorrectAnswer = editTextCorrectAnswer.text.toString()

            val hashMap = HashMap<String, Any>()
            hashMap["question"] = updateQuestion
            hashMap["optionA"] = updateOptionA
            hashMap["optionB"] = updateOptionB
            hashMap["optionC"] = updateOptionC
            hashMap["optionD"] = updateOptionD
            hashMap["correctAnswer"] = updateCorrectAnswer

            dp.collection("teacher_tests_question").document(question.questionId!!).update(hashMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Question updated successfully", Toast.LENGTH_SHORT).show()
                    getQuestions()

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update question", Toast.LENGTH_SHORT).show()
                }


        }
        alertDialog.setNegativeButton("Cancel") { _, _ ->
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()

        }

        alertDialog.setView(view)
        alertDialog.show()

    }

    override fun onDeleteClicked(
        position: Int,
        question: QuestionDataModel
    ) {

        val alertDialog=AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Question")
        alertDialog.setMessage("Are you sure you want to delete this question?")
        alertDialog.setPositiveButton("Delete") { _, _ ->
            dp.collection("teacher_tests_question").document(question.questionId!!).delete()
            questionList.removeAt(position)
            questionAdapter.notifyItemRemoved(position)
            questionAdapter.notifyItemRangeChanged(position, questionList.size)
        }
        alertDialog.setNegativeButton("Cancel") { _, _ ->
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()

        }
        alertDialog.show()

    }
}