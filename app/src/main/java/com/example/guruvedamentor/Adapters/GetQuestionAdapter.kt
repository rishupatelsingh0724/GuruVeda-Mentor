package com.example.guruvedamentor.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.DataModel.QuestionDataModel
import com.example.guruvedamentor.Interface.OnQuestionItemClickListener
import com.example.guruvedamentor.R

class GetQuestionAdapter(private val questionList: ArrayList<QuestionDataModel>, private val listener: OnQuestionItemClickListener):RecyclerView.Adapter<GetQuestionAdapter.QuestionViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionViewHolder {
        val layout=LayoutInflater.from(parent.context).inflate(R.layout.get_questions_layout,parent,false)
        return QuestionViewHolder(layout)
    }

    fun updateData(newData: List<QuestionDataModel>) {
        questionList.clear()
        questionList.addAll(newData)
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: QuestionViewHolder,
        position: Int
    ) {
      val currentItem=questionList[position]
        holder.question.text=currentItem.question
        holder.optionA.text="Option: ${currentItem.optionA}"
        holder.optionB.text="Option: ${currentItem.optionB}"
        holder.optionC.text="Option: ${currentItem.optionC}"
        holder.optionD.text="Option: ${currentItem.optionD}"
        holder.correctAnswer.text="Correct Answer: ${currentItem.correctAnswer}"

        holder.menuImageView.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, holder.menuImageView)
            popup.inflate(R.menu.question_item_menu)

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_update -> {
                        listener.onUpdateClicked(position, currentItem)
                        true
                    }
                    R.id.menu_delete -> {
                        listener.onDeleteClicked(position, currentItem)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }


    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val question=itemView.findViewById<TextView>(R.id.textViewQuestion)
            val optionA=itemView.findViewById<TextView>(R.id.textViewOptionA)
            val optionB=itemView.findViewById<TextView>(R.id.textViewOptionB)
            val optionC=itemView.findViewById<TextView>(R.id.textViewOptionC)
            val optionD=itemView.findViewById<TextView>(R.id.textViewOptionD)
            val correctAnswer=itemView.findViewById<TextView>(R.id.textViewCorrectAnswer)
            val menuImageView=itemView.findViewById<ImageView>(R.id.menuImageView)

    }
}