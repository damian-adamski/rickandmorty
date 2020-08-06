package com.example.rickandmorty.repo

import com.example.rickandmorty.retrofit.RetrofitClient

class CharactersRepository {

    suspend fun getCharactersOfPage(page: Int) = RetrofitClient.api.getCharacters(page)
}