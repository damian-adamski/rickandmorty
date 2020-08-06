package com.example.rickandmorty.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.CharactersAdapter
import com.example.rickandmorty.retrofit.CharactersAPI.Companion.PAGE_SIZE
import com.example.rickandmorty.ui.CharactersViewModel
import com.example.rickandmorty.ui.MainActivity
import com.example.rickandmorty.util.Resource
import kotlinx.android.synthetic.main.fragment_characters.*

class CharactersFragment : Fragment(R.layout.fragment_characters) {

    lateinit var viewModel: CharactersViewModel
    lateinit var charactersAdapter: CharactersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        charactersAdapter.setOnItemClickListener {
            viewModel.currentCharacter.value = it
            findNavController().navigate(R.id.action_charactersFragment_to_characterInfoFragment)
        }

        // region observing for changes
        viewModel.charactersResponseState.observe(viewLifecycleOwner, Observer {response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { charactersResponse ->
                        charactersAdapter.differ.submitList(charactersResponse.results.toList())
                        isLastPage = viewModel.charactersPage == charactersResponse.info.pages
                        if (isLastPage) {
                            rvCharacters.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(this.tag, message)
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
        //endregion
    }

    //region manipulating ProgressBar
    private fun hideProgressBar() {
        pbPagination.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        pbPagination.visibility = View.VISIBLE
        isLoading = true
    }
    //endregion

    //region setting RecyclerView
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getCharactersPage()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        charactersAdapter = CharactersAdapter()
        rvCharacters.apply {
            adapter = charactersAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(onScrollListener)
        }
    }
    //endregion
}