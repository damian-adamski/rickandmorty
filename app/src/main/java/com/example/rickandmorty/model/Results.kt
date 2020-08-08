package com.example.rickandmorty.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Results (val id: Int = -1,
               @SerializedName("name") val characterName: String = "",
               val status: String = "",
               val species: String = "",
               val gender: String = "",
               val origin: Origin = Origin(),
               val location: Location = Location(),
               val image: String = "") : Serializable {

    override fun equals(other: Any?): Boolean {

        if (this.javaClass != other!!.javaClass) return false

        other as Results
        /* więcej pól nie trzeba sprawdzać, właściwie id byłoby wystarczające, ale może być ta sama postać dwa razy z różnymi id */
        //if (this.id != other.id) return false       // id sprawdzam w areItems the same, więc szkoda, żeby tutaj już zwróciło false
        if (this.characterName != other.characterName) return false
        if (this.species != other.species) return false
        if (this.gender != other.gender) return false

        return true
    }
}