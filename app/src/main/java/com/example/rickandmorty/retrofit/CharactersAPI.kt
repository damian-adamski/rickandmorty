package com.example.rickandmorty.retrofit

import com.example.rickandmorty.model.CharactersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersAPI {

    @GET("character/")
    suspend fun getCharacters(@Query("page") page: Int = 1): Response<CharactersResponse>




    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
        const val PAGE_SIZE = 20
    }
}