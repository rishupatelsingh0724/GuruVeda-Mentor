package com.example.guruvedamentor.Adapters

import android.app.Activity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.DataModel.FreeVideosDataModel
import com.example.guruvedamentor.R
import com.example.guruvedamentor.View.FreeVideosPlayerActivity

class FreeVideosAdapter(val list: ArrayList<FreeVideosDataModel>):RecyclerView.Adapter<FreeVideosAdapter.FreeVideosViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FreeVideosViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.free_videos_item_layout,parent,false)
        return FreeVideosViewHolder(layout)
    }

    override fun onBindViewHolder(
        holder: FreeVideosViewHolder,
        position: Int
    ) {
       val item=list[position]
        holder.freeVideoTitle.text=item.title
        holder.freeVideoDescription.text=item.description
       getThumbnailFromVideoUrl(item.videoUrl.toString(),holder.freeVideoImage)
        holder.freeVideoDuration.text=item.duration

        holder.itemView.setOnClickListener {
            val intent=Intent(holder.itemView.context,FreeVideosPlayerActivity::class.java)
            intent.putExtra("videoUrl",item.videoUrl)
            intent.putExtra("videoTitle",item.title)
            intent.putExtra("videoDescription",item.description)
            intent.putExtra("videoDuration",item.duration)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class FreeVideosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        val freeVideoTitle=itemView.findViewById<TextView>(R.id.freeVideoTitle)
        val freeVideoDescription=itemView.findViewById<TextView>(R.id.freeVideoDescription)
        val freeVideoImage=itemView.findViewById<ImageView>(R.id.freeImageView)
        val freeVideoDuration=itemView.findViewById<TextView>(R.id.freeVideoDuration)

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

}