package com.example.guruvedamentor.Fragments.MyClasses.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.example.guruvedamentor.Fragments.MyClasses.DataModel.VideoDataModel
import com.example.guruvedamentor.R

class CourseVideoPlaygroundAdapter(private val context: Context, private val videoList: MutableList<VideoDataModel>): RecyclerView.Adapter<CourseVideoPlaygroundAdapter.CourseVideoPlayground>() {

    class CourseVideoPlayground(itemView: View): RecyclerView.ViewHolder(itemView) {
        val videoThumbnail: ImageView = itemView.findViewById(R.id.videoThumbnail)
        val videoDuration: TextView = itemView.findViewById(R.id.videoDuration)
        val videoTitle: TextView = itemView.findViewById(R.id.videoTitleName)
        val videoDescription: TextView = itemView.findViewById(R.id.videoDescriptionText)
        val videoType : TextView = itemView.findViewById(R.id.videoType)
        val moreOption: ImageView = itemView.findViewById(R.id.videoMoreOptions)
    }

    var playingPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseVideoPlayground {
        val view = LayoutInflater.from(context).inflate(R.layout.course_video_adapter_design, parent, false)
        return CourseVideoPlayground(view)
    }


    override fun getItemCount(): Int {
        return videoList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(
        holder: CourseVideoPlayground, position: Int) {
        val video = videoList[position]
        holder.videoTitle.text = video.title
        holder.videoDescription.text = video.description
        holder.videoDuration.text = video.duration
        holder.videoType.text = video.type
        holder.videoThumbnail.setImageResource(R.drawable.please_wait)
        getThumbnailFromVideoUrl(video.videoUrl.toString(),holder.videoThumbnail)

        if (position == playingPosition){
            holder.itemView.setBackgroundColor(context.getColor(R.color.d_black))
        }else{
            holder.itemView.setBackgroundColor(context.getColor(R.color.d_white))
        }

        holder.itemView.setOnClickListener {

            if (playingPosition == position){
                return@setOnClickListener
            }else{
                val previousPosition = playingPosition
                playingPosition = position
                val activity = context as Activity
                notifyItemChanged(previousPosition)
                notifyItemChanged(playingPosition)
                (activity).findViewById<TextView>(R.id.videoTitleTextView).text = video.title
                (activity).findViewById<TextView>(R.id.videoDescriptionTextView).text = video.description
                (activity).findViewById<PlayerView>(R.id.courseVideoPlayerView).player?.playWhenReady = true
                (activity).findViewById<PlayerView>(R.id.courseVideoPlayerView).player?.setMediaItem(MediaItem.fromUri(video.videoUrl.toString()))
                (activity).findViewById<PlayerView>(R.id.courseVideoPlayerView).player?.prepare()
                (activity).findViewById<PlayerView>(R.id.courseVideoPlayerView).player?.play()
                (activity).findViewById<PlayerView>(R.id.courseVideoPlayerView).player
                    ?.seekTo(0)

            }

        }

    }
    private fun getThumbnailFromVideoUrl(videoUrl: String, imageView: ImageView) {
        val retriever = MediaMetadataRetriever()
        Thread {
            try {
                retriever.setDataSource(videoUrl, HashMap())
                val bitmap = retriever.getFrameAtTime(1_000_000)

                (imageView.context as Activity).runOnUiThread {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                retriever.release()
            }
        }.start()

    }

}