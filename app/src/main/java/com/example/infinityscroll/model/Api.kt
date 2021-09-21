package com.example.infinityscroll.model


import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("/posts")
    fun getNextPage(@Query("_page") page: Int): Single<List<ItemData>>
}