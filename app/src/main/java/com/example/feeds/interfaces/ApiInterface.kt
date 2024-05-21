package com.example.feeds.interfaces

import com.example.feeds.models.DataModel
import retrofit2.http.*
import retrofit2.Call

interface ApiInterface {

    @GET("/api/v1/users")
    fun getUser() : Call<String>

    @POST("/api/v1/add-user")
    fun addUser() : Call<String>

    @GET("jokes/random")
    fun getJokes() : Call<DataModel>
}