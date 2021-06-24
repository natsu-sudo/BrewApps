package com.assignment.brewapps.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.assignment.brewapps.R
import com.assignment.brewapps.pojo.KeyLinks
import com.bumptech.glide.Glide


class DetailFragmentAdapter() : ListAdapter<KeyLinks, DetailFragmentAdapter.ViewHolder>(
    DiffCallbackKey()
) {
    inner class ViewHolder(private val view: View):RecyclerView.ViewHolder(view) {
        private val thumbNail:ImageView=view.findViewById(R.id.trailer_thumb_nail)
        private val playVideo:ImageView=view.findViewById(R.id.play_video)


        fun onBind(item: KeyLinks) {
            Glide.with(itemView)
                .load(view.context.getString(R.string.you_tube_thub, item.key))
                .error(R.drawable.error_default)
                .into(thumbNail)
            playVideo.setOnClickListener {
                val intent=Intent(Intent.ACTION_VIEW)
                if (intent.resolveActivity(itemView.context.packageManager)!=null){
                    itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(view.context.getString(R.string.you_tube_video_url,item.key))))
                }else{
                    Toast.makeText(itemView.context,itemView.context.getString(R.string.no_app), Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_trailer_layout,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}





class DiffCallbackKey:DiffUtil.ItemCallback<KeyLinks>(){
    override fun areItemsTheSame(oldItem: KeyLinks, newItem: KeyLinks): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: KeyLinks, newItem: KeyLinks): Boolean {
        return oldItem==newItem
    }

}
