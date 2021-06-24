package com.assignment.brewapps.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.assignment.brewapps.network.ApiService
import com.assignment.brewapps.pojo.Movies

class MobileDetailRepository(context: Context) {
    private  val TAG = "MobileDetailRepository"
    private val movieDetailRepository=MoviesDatabase.getInstance(context).nowPlayingDetailDao()
    private val apiService by lazy { ApiService.getInstance() }

    fun getMovieDetail(id:Long): LiveData<Movies> {
        return movieDetailRepository.getMoviesDetail(id)
    }

    suspend fun getVideoList(id: Long){
        val list=apiService.getMovieTrailers(id)
        Log.d(TAG, "fetchFromNetwork: $list")
        if (list.isSuccessful){
            val moviesList=list.body()
            Log.d(TAG, "fetchFromNetwork: $moviesList")

            moviesList?.let {
                Log.d(TAG, "getVideoList: "+it.results)
                var linkString=""
                    it.results.forEach {
                        Log.d(TAG, "getVideoList: "+it.key)
                    linkString+= ":${it.key}:"
                }
                Log.d(TAG, "getVideoList: $linkString")
                movieDetailRepository.updateLink(linkString,id)
            }
        }
    }




}