package com.assignment.brewapps.ui

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.assignment.brewapps.R
import com.assignment.brewapps.pojo.KeyLinks

class DetailFragmentAdapter(private val listener: (String) -> Unit) : ListAdapter<KeyLinks, DetailFragmentAdapter.ViewHolder>(
    DiffCallbackKey()
) {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    class ViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        val videoWeb=view.findViewById<WebView>(R.id.youtube_link)

        init {

        }

        fun onBind(item: KeyLinks) {
            videoWeb.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/eWEF1Zrmdow\" frameborder=\"0\" allowfullscreen></iframe>","text/html" , "utf-8" )
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_trailer_layout,
            parent,
            false
        )
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
