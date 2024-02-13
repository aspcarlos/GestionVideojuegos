package com.example.gestionvideojuegos

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionvideojuegos.databinding.ActivityInicioBinding

class InicioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Bienvenid@" // Cambiar el titulo de la pantalla

        val button = findViewById<Button>(R.id.bAcceso)
        button.setBackgroundColor(Color.BLUE)

        binding.bAcceso.setOnClickListener {
            // Accedemos al listado de videojuegos, para poder gestionarlos
            intent = Intent(this, ListadoActivity::class.java)
            startActivity(intent)
        }

    }
}