package com.assignment.brewapps.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.assignment.brewapps.R

class RatingAdapter(private val rating:Int) : RecyclerView.Adapter<RatingAdapter.ViewHolder>() {
    class ViewHolder(private val view:View):RecyclerView.ViewHolder(view) {
        fun onBind() {
            imageView.setImageResource(R.drawable.ic_baseline_star_24)
        }

        private val imageView:ImageView=view.findViewById(R.id.imageView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.rating, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int {
        return rating
    }


}
