package com.example.guruvedamentor.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.DataModel.TeacherTestScheduleDataModel
import com.example.guruvedamentor.Interface.AddQuestionInterface
import com.example.guruvedamentor.Interface.UpdateTestSchedule
import com.example.guruvedamentor.R
import com.example.guruvedamentor.View.QuestionsActivity

class TeacherTestsSchedule(var list: ArrayList<TeacherTestScheduleDataModel>,var addQuestionInterface: AddQuestionInterface,val updateTestSchedule: UpdateTestSchedule): RecyclerView.Adapter<TeacherTestsSchedule.TeacherTestsScheduleViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherTestsScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_schedule_layout, parent, false)
        return TeacherTestsScheduleViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TeacherTestsScheduleViewHolder,
        position: Int
    ) {

        val currentItem = list[position]

        holder.test_subject.text=currentItem.testSubject
        holder.test_tittle.text=currentItem.testTitle
        holder.test_description.text=currentItem.testDescription
        holder.test_duration.text= "Duration:${ currentItem.timeDuration } mins"
        holder.add_questions.setOnClickListener {
            addQuestionInterface.addQuestions(position)
        }
        holder.test_questions.setOnClickListener {
            val intent=Intent(holder.itemView.context,QuestionsActivity::class.java)
            intent.putExtra("subject",currentItem.testSubject)
            holder.itemView.context.startActivity(intent)
        }
        holder.test_edit.setOnClickListener {
            updateTestSchedule.updateTestSchedule(position)
        }
        holder.test_delete.setOnClickListener {
            updateTestSchedule.deleteTestSchedule(position)
        }

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class TeacherTestsScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val test_subject=itemView.findViewById<TextView>(R.id.test_subject)
        val test_tittle=itemView.findViewById<TextView>(R.id.test_tittle)
        val test_description=itemView.findViewById<TextView>(R.id.test_description)
        val test_duration=itemView.findViewById<TextView>(R.id.test_duration)
        val add_questions=itemView.findViewById<TextView>(R.id.add_questions)
        val test_edit=itemView.findViewById<TextView>(R.id.test_edit)
        val test_all_students=itemView.findViewById<TextView>(R.id.test_all_students)
        val test_questions=itemView.findViewById<TextView>(R.id.test_questions)
        val test_delete=itemView.findViewById<TextView>(R.id.test_delete)

    }
}