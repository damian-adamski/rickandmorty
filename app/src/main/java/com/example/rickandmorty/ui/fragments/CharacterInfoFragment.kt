package com.example.rickandmorty.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.ui.CharactersViewModel
import com.example.rickandmorty.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_character_info.*

class CharacterInfoFragment : Fragment(R.layout.fragment_character_info) {

    lateinit var viewModel: CharactersViewModel
    //val args: CharacterInfoFragmentArgs by navArgs() // chciałem przekazywać klikniętą postać przez Bundle, ale fajnie będzie obserwować zmienną z ViewModelu
    val ctx = context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        viewModel.currentCharacter.observe(viewLifecycleOwner, Observer { character ->
            run {
                Glide.with(this@CharacterInfoFragment)
                    .load(character.image)
                    .circleCrop()
                    .into(ivCharacterImage)
                tvName.text = character.characterName
                tvGender.text = character.gender
                tvStatus.text = character.status
                tvOrigin.text = character.origin.originName
                tvLocation.text = character.location.locationName
            }

        })
    }
}