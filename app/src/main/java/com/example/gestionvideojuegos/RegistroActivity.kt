package com.example.gestionvideojuegos

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionvideojuegos.database.Usuarios
import com.example.gestionvideojuegos.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var usuario: Usuarios

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Nuevo Usuario" // Cambiar el titulo de la pantalla

        val button1 = findViewById<Button>(R.id.bRegistrarse2)
        button1.setBackgroundColor(Color.BLUE)

        binding.bRegistrarse2.setOnClickListener {
            var nombreUs = binding.nombre.text.toString()
            var apellidosUs = binding.apellidos.text.toString()
            var emailUs = binding.emailR.text.toString()
            var passwordUs = binding.passwordR.text.toString()

            // Validar que los campos no esten vacios
            if(nombreUs.isEmpty() && apellidosUs.isEmpty() && emailUs.isEmpty() && passwordUs.isEmpty()){
                Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
            else
            {
                usuario = Usuarios(nombre = nombreUs, apellidos = apellidosUs, email = emailUs, password = passwordUs)

                // validar la contraseña y el correo
                if(usuario.validarContraseña(usuario) && usuario.validarEmail())
                {
                    if(usuario.existeUsuario())
                    {
                        Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                        binding.emailR.setText("")
                        binding.passwordR.setText("")
                    }
                    else
                    {
                        // Registrar usuario
                        if (!usuario.registrarUsuario(usuario))
                        {
                            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                            Intent(this, MainActivity::class.java).also {
                                startActivity(intent)
                            }
                        }
                        else
                        {
                            // limpiar campos
                            binding.nombre.setText("")
                            binding.apellidos.setText("")
                            binding.emailR.setText("")
                            binding.passwordR.setText("")

                            Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
                            Intent(this, MainActivity::class.java).also {
                                startActivity(intent)
                            }
                        }
                    }
                }
                else
                {
                    Toast.makeText(this, "La contraseña no es lo suficientemente segura", Toast.LENGTH_SHORT).show()
                    binding.passwordR.setText("")
                    binding.passwordR.requestFocus()
                }
            }
        }
    }
}