package com.assignment.brewapps.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "now_playing")
data class Movies(
    @PrimaryKey
    val id:Long,
    @ColumnInfo( name = "backdrop_path")
    @SerializedName("backdrop_path")
    val backdropPath:String,//for More than 7 votes
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    val releaseDate: Date,
    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    val title:String,
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    val posterPath:String,//for unpopular movie
    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    val overView:String,
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    val rating:Double,
    @ColumnInfo(name = "link_to_video")
    val linksToVideo:String?=null
)
