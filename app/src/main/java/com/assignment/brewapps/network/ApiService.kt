package com.assignment.brewapps.network

import androidx.viewbinding.BuildConfig
import com.assignment.brewapps.Constants
import com.assignment.brewapps.pojo.Movies
import com.assignment.brewapps.pojo.MoviesLink
import com.assignment.brewapps.pojo.NowPlayingList
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    companion object{


        private val retrofitService by lazy {
            val gson= GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService::class.java)
        }

        fun getInstance():ApiService= retrofitService
    }
    @GET("movie/{id}?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed")
    suspend fun getMovieList(@Path("id")id:String): Response<NowPlayingList>

    @GET("movie/{id}/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed")
    suspend fun getMovieTrailers(@Path("id")id:Long): Response<MoviesLink>




}