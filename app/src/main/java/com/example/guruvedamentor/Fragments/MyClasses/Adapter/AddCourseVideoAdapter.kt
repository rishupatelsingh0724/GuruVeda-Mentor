package com.example.guruvedamentor.Fragments.MyClasses.Adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.Fragments.MyClasses.DataModel.VideoDataModel
import com.example.guruvedamentor.Fragments.MyClasses.view.AddNewCourseVideoAndUpdateActivity
import com.example.guruvedamentor.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddCourseVideoAdapter(private val context: Context,private val videoList: MutableList<VideoDataModel>,private val courseId: String):RecyclerView.Adapter<AddCourseVideoAdapter.AddCourseVideoViewHolder>() {
    class AddCourseVideoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val videoThumbnail: ImageView = itemView.findViewById(R.id.videoThumbnail)
        val videoDuration: TextView = itemView.findViewById(R.id.videoDuration)
        val videoTitle: TextView = itemView.findViewById(R.id.videoTitleName)
        val videoDescription: TextView = itemView.findViewById(R.id.videoDescriptionText)
        val videoType : TextView = itemView.findViewById(R.id.videoType)
        val moreOption: ImageView = itemView.findViewById(R.id.videoMoreOptions)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCourseVideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.course_video_adapter_design, parent, false)
        return AddCourseVideoViewHolder(view)
    }

    override fun getItemCount(): Int {
       return videoList.size
    }
    
    override fun onBindViewHolder(holder: AddCourseVideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.videoTitle.text = video.title
        holder.videoDescription.text = video.description
        holder.videoDuration.text = video.duration
        holder.videoType.text = video.type
        holder.videoThumbnail.setImageResource(R.drawable.please_wait)
        getThumbnailFromVideoUrl(video.videoUrl.toString(),holder.videoThumbnail)

        holder.moreOption.setOnClickListener {
            val popupMenu = android.widget.PopupMenu(context, holder.moreOption)
            popupMenu.inflate(R.menu.popup_menuof_video)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.updateVideo -> {
                        val intent = Intent(context, AddNewCourseVideoAndUpdateActivity::class.java)
                        intent.putExtra("isUpdate", true)
                        intent.putExtra("videoId", video.id)
                        intent.putExtra("videoTitle", video.title)
                        intent.putExtra("videoDescription", video.description)
                        intent.putExtra("videoUrl", video.videoUrl)
                        intent.putExtra("videoDuration", video.duration)
                        intent.putExtra("videoType", video.type)
                        intent.putExtra("courseId", courseId)
                        intent.putExtra("duration", video.duration)
                        context.startActivity(intent)
                        true
                    }

                    R.id.deleteVideo -> {
                        showDeleteConfirmationDialog(video.videoUrl.toString(),video.id.toString(),position)
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }

    }
    private fun getThumbnailFromVideoUrl(videoUrl: String, imageView: ImageView) {
        val retriever = MediaMetadataRetriever()
        Thread {
            try {
                retriever.setDataSource(videoUrl, HashMap())
                val bitmap = retriever.getFrameAtTime(1_000_000)
                retriever.release()

                (imageView.context as Activity).runOnUiThread {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun deleteVideoFromFirebase(videoUrl: String,videoId: String,position: Int){
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(videoUrl)
        storageRef.delete().addOnSuccessListener {
            FirebaseFirestore.getInstance()
                .collection("courses")
                .document(courseId)
                .collection("videos")
                .document(videoId)
                .delete()
                .addOnSuccessListener {
                    videoList.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Video Deleted Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Storage Delete Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun showDeleteConfirmationDialog(videoUrl: String, videoId: String,position: Int) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Delete Video")
            .setMessage("Kya aap sach me is video ko delete karna chahte hain?")
            .setPositiveButton("Delete") { _, _ ->
                deleteVideoFromFirebase(videoUrl, videoId,position)
            }
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.show()
    }


}