package com.example.gestionvideojuegos

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionvideojuegos.database.Usuarios
import com.example.gestionvideojuegos.databinding.ActivityOlvidoPasswordBinding

class OlvidoPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOlvidoPasswordBinding
    private lateinit var usuario: Usuarios

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOlvidoPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuario = Usuarios(nombre = "", apellidos = "", email = "", password = "")

        val button1 = findViewById<Button>(R.id.bRecordarPassword)
        button1.setBackgroundColor(Color.BLUE)

        binding.bRecordarPassword.setOnClickListener {
            var email = binding.email2.text.toString()

            // Validar el correo electronico
            if(email.isEmpty())
            {
                Toast.makeText(this, "Es necesario rellenar el campo de Email", Toast.LENGTH_SHORT).show()
            }
            else
            {
                usuario.email = email
                if(!usuario.recordarContraseña(email))
                {
                    // Si el usuario se ha registrado correctamente, mostraremos un mensaje de que se ha enviado el mensaje a su email
                    Toast.makeText(this, "Se ha enviado un correo electronico a su email", Toast.LENGTH_SHORT).show()

                    // Volver a la pantalla de login (para poder volver a escribir la nueva contraseña para logearse)
                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this, "No se ha podido enviar ningun mensaje. Vuelvelo a intentar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}