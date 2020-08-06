package com.example.rickandmorty.util


/**
 * Mam większą kontrolę nad odpowiedziami z serwera
 * Przy obserwowaniu na poziomie widoków sprawdzam jakiej klasy obiekt otrzymałem
 * i mogę podjąć odpowiednie działania z otrzymanymi danymi
 * */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T> (data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>() : Resource<T>()
}