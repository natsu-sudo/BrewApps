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
    private var deleteMovies= MutableLiveData<Movies>()
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
         deleteMovies.value=movies
           viewModelScope.launch {
               withContext(Dispatchers.IO){
                   movieDatabase.deleteSingleMovie(movies )
               }
           }
    }

    val deleteMovie:LiveData<List<Movies>> =Transformations.switchMap(deleteMovies,
    ::getListFrom)

    private fun getListFrom(movies: Movies): LiveData<List<Movies>> {
        return movieDatabase.getMovieList()
    }

    private fun searchMovies(search:String):LiveData<List<Movies>> {
        return movieDatabase.searchQuery(search)
    }

    fun setSearch(search: String) {
            searchMovies.value=search
    }
}