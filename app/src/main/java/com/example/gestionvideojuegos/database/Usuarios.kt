package com.example.gestionvideojuegos.database

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore

open class Usuarios {

    // Miembros de la clase Usuario
    var nombre: String = ""
    var apellidos: String = ""
    var email: String = ""
    var password: String = ""

    // Constructor de la clase Usuario (por defecto)
    constructor(
        nombre: String,
        apellidos: String,
        email: String,
        password: String,
    )
    {
        this.nombre = nombre
        this.apellidos = apellidos
        this.email = email
        this.password = password
    }

    // METODOS

    // Metodo para obtener los datos del usuario
    override fun toString(): String {
        return "Usuario(nombre='$nombre', apellidos='$apellidos', email='$email', password='$password')"
    }

    // Metodo para validar la contraseña del usuario para que sea segura
    fun validarContraseña(usuario: Usuarios): Boolean {

        // Inicializamos la contraseña a false inicialmente, porque suponemos que no estara bien en principio
        var esValida = false

        // Validar que la contraseña tenga al menos 8 caracteres
        if (usuario.password.length < 8) {
            esValida = true
        }

        // Validar que la contraseña tenga al menos una letra mayúscula
        if (usuario.password.contains(Regex("[A-Z]"))) {
            esValida = true
        }

        // Validar que la contraseña tenga al menos una letra minúscula
        if (usuario.password.contains(Regex("[a-z]"))) {
            esValida = true
        }

        // Validar que la contraseña tenga al menos un número
        if (usuario.password.contains(Regex("[0-9]"))) {
            esValida = true
        }

        // Validar que la contraseña tenga al menos un carácter especial
        if (usuario.password.contains(Regex("[!@#$%^&*()_+-={}\\[\\]|;:'\",.<>/?]"))) {
            esValida = true
        }

        return esValida
    }

    // Metodo para validar el email
    fun validarEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Metodo para el Registro de usuario en la base de datos de Firebase
    fun registrarUsuario(usuario: Usuarios) : Boolean {
        var registrado = false

        // Obtener la instancia de FirebaseAuth
        val Auth = FirebaseAuth.getInstance()

        val id = Integer.toString(usuario.hashCode())

        // Crear un nuevo usuario con la dirección de correo electrónico y la contraseña proporcionadas
        Auth.createUserWithEmailAndPassword(usuario.email, usuario.password)
            .addOnCompleteListener { task ->
                // Comprobar si el registro se ha realizado correctamente
                if (task.isSuccessful) {
                    // Guardar los datos del usuario en la base de datos de Firebase
                    val db = Firebase.firestore

                    // Creamos un mapa con los datos del usuario
                    val datosUs = hashMapOf(
                        "nombre" to usuario.nombre,
                        "apellidos" to usuario.apellidos,
                        "email" to usuario.email,
                        "password" to usuario.password,
                        "ID" to id
                    )

                    // Añadimos los datos al documento
                    db.collection("usuarios").document(id).set(datosUs)
                        .addOnSuccessListener { documentReference ->
                            Log.d("Usuario", "Registro de usuario exitoso con ID: ${documentReference}")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Usuario", "Error al registrar al usuario", e)
                        }
                    // El registro se ha realizado correctamente
                    registrado = true

                } else {
                    // El registro no se ha realizado correctamente
                    registrado = false
                }

            }

        return registrado

    }

    fun existeUsuario(): Boolean {
        var existe = false
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")
        myRef.get().addOnSuccessListener {
            if (it.hasChild(email)) {
                existe = true
            }
        }
        return existe
    }

    open fun recordarContraseña(email: String) : Boolean {
        var enviado = false
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnSuccessListener { task ->
                enviado = true
            }
            .addOnFailureListener() {
                enviado = false
            }

        return enviado
    }

}