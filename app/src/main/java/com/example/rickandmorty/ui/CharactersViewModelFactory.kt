package com.example.rickandmorty.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.repo.CharactersRepository

/** Żebym mógł przesłać repo w konstruktorze */
class CharactersViewModelFactory(val app: Application,
                                 val charactersRepo: CharactersRepository
) :  ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CharactersViewModel(app, charactersRepo) as T
    }
}