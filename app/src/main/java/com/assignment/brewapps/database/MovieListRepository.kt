package com.assignment.brewapps.database

import android.content.Context
import android.provider.SyncStateContract
import android.util.Log
import androidx.lifecycle.LiveData
import com.assignment.brewapps.Constants
import com.assignment.brewapps.network.ApiService
import com.assignment.brewapps.pojo.ErrorCode
import com.assignment.brewapps.pojo.LoadingStatus
import com.assignment.brewapps.pojo.Movies
import java.net.UnknownHostException

class MovieListRepository(context: Context) {
    private val moviesListRepository=MoviesDatabase.getInstance(context).nowPlayingListDao()
    private val apiService by lazy { ApiService.getInstance() }

    fun getMovieList():LiveData<List<Movies>>{
        return moviesListRepository.getMovieListFromDB()
    }

    suspend fun deleteFromDataBase(){
        moviesListRepository.deleteAllData()
    }

    suspend fun deleteSingleMovie(movies:Movies){
         moviesListRepository.delete(movies)
    }

     fun searchQuery(search:String):LiveData<List<Movies>>{
        return moviesListRepository.searchForList(search)
    }

    suspend fun fetchFromNetwork()=try {
        val result=apiService.getMovieList(Constants.NOW_PLAYING)
        Log.d("TAG", "fetchFromNetwork: $result")
        if (result.isSuccessful){
            val tvSeriesList=result.body()
            Log.d("TAG", "fetchFromNetwork: $tvSeriesList")
            tvSeriesList?.let {
                Log.d("TAG", "fetchFromNetwork: "+it.results.size)
                moviesListRepository.insertIntoDb(it.results) }
            LoadingStatus.success()
        }else{
            LoadingStatus.error(ErrorCode.NO_DATA)
        }
    }catch (ex: UnknownHostException){
        LoadingStatus.error(ErrorCode.NETWORK_ERROR)
    }catch (ex: Exception){
        LoadingStatus.error(ErrorCode.UNKNOWN_ERROR)
    }






}