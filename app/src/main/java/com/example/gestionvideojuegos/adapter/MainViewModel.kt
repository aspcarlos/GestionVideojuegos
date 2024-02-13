package com.example.gestionvideojuegos.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val repo = Repo()
    fun fetchVideojuegoData(): LiveData<MutableList<Videojuego>> {
        val mutableData = MutableLiveData<MutableList<Videojuego>>()
        repo.getVideojuegoData().observeForever { videojuegoList ->
            mutableData.value = videojuegoList as MutableList<Videojuego>
        }
        return mutableData

    }
}