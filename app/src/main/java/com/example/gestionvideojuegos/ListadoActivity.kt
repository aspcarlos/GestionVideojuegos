package com.example.gestionvideojuegos

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestionvideojuegos.adapter.MainAdapter
import com.example.gestionvideojuegos.adapter.MainViewModel
import com.example.gestionvideojuegos.adapter.Videojuego
import com.example.gestionvideojuegos.databinding.ActivityListadoBinding


class ListadoActivity : ActivityWithMenu() {

    private lateinit var binding: ActivityListadoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private var videojuegoList = mutableListOf<Videojuego>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeData()

        val button1 = findViewById<Button>(R.id.bAnadirVideojuego)
        button1.setBackgroundColor(Color.BLUE)

        val button2 = findViewById<Button>(R.id.bEliminarVideojuego)
        button2.setBackgroundColor(Color.BLUE)

        // Imprime la lista inicial para verificar que se esté inicializando correctamente
        videojuegoList.forEach { videojuego -> Log.d("Videojuego", "Nombre del videojuego: ${videojuego.nombre}") }

        binding.etBuscar.addTextChangedListener { filtroVideojuego ->
            Log.d("Filtro", "Texto de búsqueda: $filtroVideojuego")
            val videojuegosFiltered = videojuegoList.filter { videojuego ->
                videojuego.nombre.lowercase().contains(filtroVideojuego.toString().lowercase())
            }
            Log.d("Filtro", "Número de elementos filtrados: ${videojuegosFiltered.size}")
            adapter.updateVideogames(videojuegosFiltered)
        }

        // BOTONES
        binding.bAnadirVideojuego.setOnClickListener {
            intent = Intent(this, AñadirVideojuegoActivity::class.java)
            startActivity(intent)
        }

        binding.bEliminarVideojuego.setOnClickListener {
            intent = Intent(this, EliminarVideojuegoActivity::class.java)
            startActivity(intent)
        }

    }

    fun observeData(){
        viewModel.fetchVideojuegoData().observe(this, Observer {
            adapter.setListData(it)
        })
    }

    fun setupRecyclerView(){
        adapter = MainAdapter(this, videojuegoList)

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

}

