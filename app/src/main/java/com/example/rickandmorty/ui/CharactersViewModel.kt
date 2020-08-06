package com.example.rickandmorty.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.appcore.AppCore
import com.example.rickandmorty.model.CharactersResponse
import com.example.rickandmorty.model.Results
import com.example.rickandmorty.repo.CharactersRepository
import com.example.rickandmorty.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CharactersViewModel(app: Application,
                          val repo: CharactersRepository
) : AndroidViewModel(app) {

    var charactersPage = 1
    val charactersResponseState: MutableLiveData<Resource<CharactersResponse>> = MutableLiveData()
    var charactersResponse: CharactersResponse? = null
    var currentCharacter: MutableLiveData<Results> = MutableLiveData() // dla CharacterInfoFragment

    init {
        getCharactersPage()
    }

    fun getCharactersPage() = viewModelScope.launch {
        charactersResponseState.postValue(Resource.Loading())
        makeSafeCall()
    }

    private fun handleResponse(response: Response<CharactersResponse>) : Resource<CharactersResponse> {
        if (response.isSuccessful) {
            response.body()?.let {result ->
                charactersPage += 1        //zwiększam numer strony tylko po pomyślnym wywołaniu
                if (charactersResponse == null) {   // przy pierwszym wywołaniu metody 'true'
                    charactersResponse = result
                } else {            // przy każdym kolejnym zapisuję poprzednią zawartość listy i dodaję do niej nowe elementy
                    val prevPage = charactersResponse?.results
                    val currentPage = result.results
                    prevPage?.addAll(currentPage)
                }
                return Resource.Success(charactersResponse ?: result)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun makeSafeCall() {    // with internet connection and try/catch block
        try {
            if (isConnected()) {
                val response =
                    repo.getCharactersOfPage(charactersPage) // suspended, zaczeka z wykonywaniem, aż dostanie odpowiedź od API
                charactersResponseState.postValue(handleResponse(response))
            } else {
                charactersResponseState.postValue(Resource.Error("No internet connection!"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> charactersResponseState.postValue(Resource.Error("Api connection failure"))
                else -> charactersResponseState.postValue(Resource.Error("JSON conversion error"))
            }
        }
    }

    private fun isConnected() : Boolean {
        val connManager: ConnectivityManager = getApplication<AppCore>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connManager.activeNetwork ?: return false               // api > 23
        val capabilities = connManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}