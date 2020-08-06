package com.example.rickandmorty.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.repo.CharactersRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: CharactersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = CharactersRepository()
        val viewModelFactory = CharactersViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CharactersViewModel::class.java)
        bottomNavigationView.setupWithNavController(charactersNavHostFragment.findNavController())
    }
}