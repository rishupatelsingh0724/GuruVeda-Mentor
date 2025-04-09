package com.example.guruvedamentor.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.Adapters.TeacherTestsSchedule
import com.example.guruvedamentor.DataClass.TeacherTestScheduleDataModel
import com.example.guruvedamentor.Interface.AddQuestionInterface
import com.example.guruvedamentor.Interface.UpdateTestSchedule
import com.example.guruvedamentor.R
import com.example.guruvedamentor.View.CreateTestActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class ManageTestsFragment : Fragment(), AddQuestionInterface, UpdateTestSchedule {

    lateinit var db: FirebaseFirestore
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
    var testScheduleList = ArrayList<TeacherTestScheduleDataModel>()
    lateinit var adapter: TeacherTestsSchedule
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manage_tests, container, false)
        recyclerView = view.findViewById(R.id.test_recyclerView)

        db = FirebaseFirestore.getInstance()
        adapter = TeacherTestsSchedule(testScheduleList, this,this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        val createTestBtn = view.findViewById<FloatingActionButton>(R.id.create_test_btn)
        createTestBtn.setOnClickListener {
            val intent = Intent(requireContext(), CreateTestActivity::class.java)
            startActivity(intent)

        }


        getTestSchedule()
        return view

    }


    fun getTestSchedule(){
        db.collection("teacher_tests_schedule")
            .get()
            .addOnSuccessListener { result ->
                testScheduleList.clear()
               for (dataSnapshot in result){
                   val testSchedule=dataSnapshot.toObject(TeacherTestScheduleDataModel::class.java)
                   testScheduleList.add(testSchedule)
                   adapter.notifyDataSetChanged()
               }
            }
    }

    override fun addQuestions(position: Int) {

        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Add Questions")
        alertDialog.setMessage("Are you sure you want to add questions?")
        val layout =
            LayoutInflater.from(requireContext()).inflate(R.layout.add_question_layout, null, false)
        val editTextQuestion = layout.findViewById<EditText>(R.id.editTextQuestion)
        val editTextOptionA = layout.findViewById<EditText>(R.id.editTextOptionA)
        val editTextOptionB = layout.findViewById<EditText>(R.id.editTextOptionB)
        val editTextOptionC = layout.findViewById<EditText>(R.id.editTextOptionC)
        val editTextOptionD = layout.findViewById<EditText>(R.id.editTextOptionD)
        val editTextCorrectAnswer = layout.findViewById<EditText>(R.id.editTextCorrectAnswer)
        alertDialog.setPositiveButton("Add") { _, _ ->
            val question = editTextQuestion.text.toString()
            val optionA = editTextOptionA.text.toString()
            val optionB = editTextOptionB.text.toString()
            val optionC = editTextOptionC.text.toString()
            val optionD = editTextOptionD.text.toString()
            val correctAnswer = editTextCorrectAnswer.text.toString()
            val questionId = UUID.randomUUID().toString()

            val questionMap = hashMapOf(
                "teacherId" to userId,
                "questionId" to questionId,
                "question" to question,
                "optionA" to optionA,
                "optionB" to optionB,
                "optionC" to optionC,
                "optionD" to optionD,
                "correctAnswer" to correctAnswer
            )

            db.collection("teacher_tests_question").document(questionId)
                .set(questionMap)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Questions added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()

                }
        }
        alertDialog.setNegativeButton("Cancel") { _, _ ->
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
        }
        alertDialog.setView(layout)
        alertDialog.show()


    }

    @SuppressLint("MissingInflatedId")
    override fun updateTestSchedule(position: Int) {

        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Update Test Schedule")
        alertDialog.setMessage("Are you sure you want to update test schedule?")
        val layout =
            LayoutInflater.from(requireContext()).inflate(R.layout.update_test_schedule_layout, null, false)

        val editTextTestTitle = layout.findViewById<EditText>(R.id.updateTextTestTitle)
        val editTextTestDescription = layout.findViewById<EditText>(R.id.updateTextTestDescription)
        val editTextTimeDuration = layout.findViewById<EditText>(R.id.updateTextTimeDuration)
        val editTextSubjects = layout.findViewById<EditText>(R.id.updateTextSubjects)

        editTextTestTitle.setText(testScheduleList[position].testTitle)
        editTextTestDescription.setText(testScheduleList[position].testDescription)
        editTextTimeDuration.setText(testScheduleList[position].timeDuration)
        editTextSubjects.setText(testScheduleList[position].testSubject)

        alertDialog.setPositiveButton("Update") { _, _ ->
            val testTitle = editTextTestTitle.text.toString()
            val testDescription = editTextTestDescription.text.toString()
            val timeDuration = editTextTimeDuration.text.toString()
            val testSubject = editTextSubjects.text.toString()

            val questionMap = hashMapOf(
                "testId" to userId,
                "testTitle" to testTitle,
                "testSubject" to testSubject,
                "testDescription" to testDescription,
                "timeDuration" to timeDuration
            )

            db.collection("teacher_tests_schedule").document(userId)
                .update(questionMap as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Tests Schedule updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "update Failed", Toast.LENGTH_SHORT).show()

                }
        }
        alertDialog.setNegativeButton("Cancel") { _, _ ->
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
        }
        alertDialog.setView(layout)
        alertDialog.show()
    }

    override fun deleteTestSchedule(position: Int) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete Test Schedule")
        alertDialog.setMessage("Are you sure you want to delete test schedule?")
        alertDialog.setPositiveButton("Delete") { _, _ ->
            db.collection("teacher_tests_schedule").document(userId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Tests Schedule deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
        alertDialog.setNegativeButton("Cancel") { _, _ ->
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
        }
        alertDialog.show()
    }

}