package com.assignment.brewapps.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.assignment.brewapps.database.MovieListRepository
import com.assignment.brewapps.pojo.LoadingStatus
import com.assignment.brewapps.pojo.Movies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListViewModel(context: Context):ViewModel() {
    private val movieDatabase= MovieListRepository(context)
    private var liveStatus= MutableLiveData<LoadingStatus>()
    private var searchMovies=MutableLiveData<String>()
    val status get() = liveStatus

    val getList: LiveData<List<Movies>> = getMovieList()
    val searchResult=Transformations.switchMap(searchMovies, ::searchMovies)


    private fun getMovieList(): LiveData<List<Movies>> {
        return movieDatabase.getMovieList()
    }

    fun fetchFromNetwork(){
        liveStatus.value=LoadingStatus.loading()
        viewModelScope.launch {
            liveStatus.value = withContext(Dispatchers.IO){
                movieDatabase.fetchFromNetwork()
            }!!
        }
    }

    fun deleteData() {
        viewModelScope.launch {
            movieDatabase.deleteFromDataBase()
        }
    }

     fun deleteSingleMovie(movies:Movies){
           viewModelScope.launch {
               withContext(Dispatchers.IO){
                   movieDatabase.deleteSingleMovie(movies )
               }
           }
    }

    private fun searchMovies(search:String):LiveData<List<Movies>> {
        return movieDatabase.searchQuery(search)
    }

    fun setSearch(search: String) {
            searchMovies.value=search
    }
}