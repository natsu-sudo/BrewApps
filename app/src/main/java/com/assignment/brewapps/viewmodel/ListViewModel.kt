package com.assignment.brewapps.viewmodel

import android.content.Context
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

    private fun searchMovies(search:String):LiveData<List<Movies>> {
        return movieDatabase.searchQuery(search)
    }



    fun setSearch(search: String) {
            searchMovies.value=search
    }

    fun deleteFromListAndMovieDb(adapterPosition: Int) {
        val deleteMovie:Movies = getList.value?.get(adapterPosition)!!
        viewModelScope.launch {
            movieDatabase.deleteSingleMovie(deleteMovie)
        }


    }
}