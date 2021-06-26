package com.assignment.brewapps.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assignment.brewapps.Constants
import com.assignment.brewapps.R
import com.assignment.brewapps.pojo.Movies
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

class MovieListAdapterList(private val listener: (Long) -> Unit):
    RecyclerView.Adapter<MovieListAdapterList.ViewHolder>() {
    inner class ViewHolder(private val view: View):RecyclerView.ViewHolder(view) {
        private val higherCard: MaterialCardView =view.findViewById(R.id.more_than_7_card)
        private val lowerCard: MaterialCardView =view.findViewById(R.id.less_than_7_card)
        private val backDropImage: ImageView =view.findViewById(R.id.vote_more_than_seven)
        private val moviePosterForLowerCard: ImageView =view.findViewById(R.id.movie_poster)
        private val movieName: TextView =view.findViewById(R.id.movie_name_less)
        private val movieOverView: TextView =view.findViewById(R.id.movie_over_view_less)
        private val higherPosterMovieName: TextView =view.findViewById(R.id.higher_poster_movie_name)

        init {
            itemView.setOnClickListener {
                listener.invoke(listOfMovies[adapterPosition].id)
            }
            itemView.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    Log.d("TAG", "onTouch: "+event!!.action)
                    when (event!!.action) {
                        MotionEvent.ACTION_MOVE -> {
                            higherPosterMovieName.visibility=View.VISIBLE
                            return true
                        }
                        MotionEvent.ACTION_CANCEL -> {
                            higherPosterMovieName.visibility=View.GONE
                            return true
                        }
                    }
                    return false
                }
            })

        }


        fun onBind(item: Movies) {
            if (item.rating>=7){
                showHigherRatingCards(item)
            }else{
                showLowerRatingCards(item)
            }

        }

        private fun showLowerRatingCards(item: Movies) {
            higherCard.visibility=View.GONE
            Glide.with(view)
                .load(Constants.POSTER_BASE_URL+item.posterPath)
                .error(R.drawable.ic_launcher_background)
                .into(moviePosterForLowerCard)
            movieName.text=item.title
            movieOverView.text=item.overView
        }

        private fun showHigherRatingCards(item: Movies) {
            lowerCard.visibility=View.GONE
            higherPosterMovieName.text=item.title
            Glide.with(view)
                .load(Constants.BACKDROP_BASE_URL+item.backdropPath)
                .error(R.drawable.ic_launcher_background)
                .override(500,500)
                .into(backDropImage)
        }
    }

    private var listOfMovies= mutableListOf<Movies>()

    fun setList(list:MutableList<Movies>){
        listOfMovies=list
        Log.d("TAG", "addList: 2 "+listOfMovies.size)
    }

    fun addList(list: MutableList<Movies>){
        list.forEach {
            listOfMovies.add(it)
        }
        Log.d("TAG", "addList: "+listOfMovies.size)
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lists, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(listOfMovies[position])
    }

    override fun getItemCount(): Int {
        return listOfMovies.size
    }
}