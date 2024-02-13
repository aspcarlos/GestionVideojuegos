package com.example.gestionvideojuegos.database

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

open class Videojuegos {
    // Miembros de la clase Videojuegos
    var nombre: String = ""
    var descripcion: String = ""
    var foto: String = ""
    var ID: String = ""

    // Constructor de la clase Videojuegos (por defecto)
    constructor(
        nombre: String,
        descripcion: String,
        foto: String
    )
    {
        this.nombre = nombre
        this.descripcion = descripcion
        this.foto = foto
    }

    // Constructor de la clase Videojuegos para Firebase
    constructor(nombre: String, descripcion: String, foto: String, id: String) {
        this.nombre = nombre
        this.descripcion = descripcion
        this.foto = foto
        this.ID = id

    }

    // METODOS
    // Metodo para obtener los datos del videojuego
    override fun toString(): String {
        return "Videojuego(nombre='$nombre', descripcion='$descripcion', foto='$foto')"
    }

    // Metodo para insertar un videojuego en la base de datos
    fun InsertarVideojuego(videojuego: Videojuegos, imagenUri: Uri): Boolean {
        var insertadoCorrectamente = false
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val idDocumento = Integer.toString(auth.currentUser?.uid?.hashCode()!! + System.currentTimeMillis().toInt())
        videojuego.ID = videojuego.hashCode().toString()

        auth.currentUser?.let {
            // Convierto los datos de la incidencia en un HashMap
            val datosVideoJ = hashMapOf(
                "nombre" to videojuego.nombre,
                "descripcion" to videojuego.descripcion,
                "ID" to videojuego.ID,
                "foto" to videojuego.foto
            )

            // Inserto los datos en la BD de Firebase
            db.collection("videojuegos").document(idDocumento).set(datosVideoJ)
                .addOnSuccessListener {
                    // Continuar con la inserción en Firestore
                    val imagesRef = storageReference.child("imagenes/")
                    val uploadTask = imagesRef.putFile(imagenUri)

                    uploadTask.addOnSuccessListener {
                        // Imagen subida exitosamente, ahora guarda el enlace de la imagen en Firestore
                        imagesRef.downloadUrl.addOnSuccessListener { uri ->
                            videojuego.foto = uri.toString()
                            // Actualizar el documento en Firestore con la nueva URL de la imagen
                            db.collection("videojuegos").document(idDocumento)
                                .update("foto", uri.toString())
                                .addOnSuccessListener {
                                    insertadoCorrectamente = true
                                    Log.d("Videojuego", "Videojuego insertado correctamente")
                                }
                                .addOnFailureListener {
                                    insertadoCorrectamente = false
                                    Log.d("Videojuego", "Error al actualizar la URL de la imagen en Firestore")
                                }
                        }
                    }.addOnFailureListener {
                        // Maneja el fallo al subir la imagen
                        Log.d("Videojuego", "Error al subir la imagen: $it")
                        insertadoCorrectamente = false
                    }
                }
                .addOnFailureListener {
                    insertadoCorrectamente = false
                    Log.d("Videojuego", "Error al insertar el Videojuego en Firestore")
                }
        }
        return insertadoCorrectamente
    }



    // Metodo para eliminar un videojuego de la base de datos

}