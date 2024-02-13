package com.example.gestionvideojuegos

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.gestionvideojuegos.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        screenSplash.setKeepOnScreenCondition { false }
        Thread.sleep(2000)

        // Ponerle color a los botones
        val button1 = findViewById<Button>(R.id.bRegistrarse)
        button1.setBackgroundColor(Color.BLUE)

        val button2 = findViewById<Button>(R.id.bRecordar)
        button2.setBackgroundColor(Color.BLUE)

        val button3 = findViewById<Button>(R.id.bIniciarSesion)
        button3.setBackgroundColor(Color.BLUE)

        // Evento Click del boton REGISTRAR
        binding.bRegistrarse.setOnClickListener {
            intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // Evento Click del boton RECUPERAR CONTRASEÑA
        binding.bRecordar.setOnClickListener {
            intent = Intent(this, OlvidoPasswordActivity::class.java)
            startActivity(intent)
        }

        // Evento Click del boton INICIAR SESION
        binding.bIniciarSesion.setOnClickListener {
            // Al pulsar sobre el boton INICIAR SESION, comprobamos autentificacion
            // pasandole a Firebase el correo y la contraseña introducida,
            // esto lo haremos mediante el metodo login()
            login()
        }

    }

    // Metodo del boton INICIAR SESION
    private fun login() {
        // Si el correo y el password no son campos vacios:
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()) {
            // Iniciamos sesion con el metodo signIn y enviamos a Firebase el correo y la contraseña
            FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString())
                .addOnCompleteListener {
                    // Si la autentificacion tuvo exito:
                    if(it.isSuccessful) {

                        var nombre = email.substring(0,email.indexOf("@"))

                        // Accedemos a la pantalla InicioActivity, para dar la bienvenida al usuario
                        val intent = Intent(this, InicioActivity::class.java)
                        intent.putExtra("email",email)
                        intent.putExtra("nombre",nombre)
                        startActivity(intent)
                    } else {
                        // sino avisamos al usuario que ocurrio un problema
                        Toast.makeText(this, "Correo o password incorrecto", Toast.LENGTH_SHORT).show()
                    }
                }
        } else { Toast.makeText(this, "Algun campo esta vacio", Toast.LENGTH_SHORT).show() }
    }
}