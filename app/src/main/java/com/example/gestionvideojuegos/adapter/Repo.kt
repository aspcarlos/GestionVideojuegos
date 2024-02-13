package com.example.gestionvideojuegos.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class Repo {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val videojuegosRef = firestore.collection("videojuegos")

    fun getVideojuegoData(): LiveData<List<Videojuego>> {
        val mutableData = MutableLiveData<List<Videojuego>>()

        videojuegosRef.get().addOnCompleteListener { result ->
            if (result.isSuccessful) {
                val listData = mutableListOf<Videojuego>()
                for (document in result.result) {
                    val foto = document.getString("foto")
                    val nombre = document.getString("nombre")
                    val descripcion = document.getString("descripcion")
                    val videojuego = Videojuego(foto!!, nombre!!, descripcion!!)
                    listData.add(videojuego)
                }
                mutableData.value = listData
            } else {
                mutableData.value = emptyList()
            }
        }

        return mutableData
    }
}