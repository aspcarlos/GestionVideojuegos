package com.example.gestionvideojuegos

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.gestionvideojuegos.databinding.ActivityEliminarVideojuegoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EliminarVideojuegoActivity : ActivityWithMenu() {

    private lateinit var binding: ActivityEliminarVideojuegoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEliminarVideojuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button = findViewById<Button>(R.id.bEliminar)
        button.setBackgroundColor(Color.BLUE)

        fun BorrarVideojuego(nombreVideojuego: String): Boolean {
            var borradoCorrectamente = false
            val auth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()

            auth.currentUser?.let {
                db.collection("videojuegos")
                    .whereEqualTo("nombre", nombreVideojuego)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            db.collection("videojuegos").document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    borradoCorrectamente = true
                                    Log.d("Videojuego", "Se ha eliminado el videojuego correctamente")
                                }
                                .addOnFailureListener {
                                    borradoCorrectamente = false
                                    Log.d("Videojuego", "Error al borrar el videojuego")
                                }
                        }
                    }
            }
            return borradoCorrectamente
        }

        binding.bEliminar.setOnClickListener {
            // Asignamos el contenido de las editText a variables
            val nombreVidEliminar = binding.tbNombreVidEliminar.text.toString()

            if(!BorrarVideojuego(nombreVidEliminar)){
                // limpiar campos
                binding.tbNombreVidEliminar.setText("")
            }
            else
            {
                finish() // Cierra la actividad actual y vuelve a la anterior
            }
        }
    }
}