package com.assignment.brewapps.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.assignment.brewapps.database.MobileDetailRepository
import com.assignment.brewapps.pojo.LoadingStatus
import com.assignment.brewapps.pojo.Movies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelDetail(context: Context): ViewModel() {
    private val detailRepository=MobileDetailRepository(context)
    private var movieId=MutableLiveData<Long>()

    fun fetchFromNetwork(id:Long){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                detailRepository.getVideoList(id)
            }!!
        }
    }

    fun setMovieId(id: Long){
        movieId.value=id
    }



    fun getDetail(id: Long):LiveData<Movies>{
        return detailRepository.getMovieDetail(id)

    }

    val getMovies=Transformations.switchMap(movieId,::getMovieDetail)

    private fun getMovieDetail(id: Long): LiveData<Movies> {
        return detailRepository.getMovieDetail(id)
    }


}