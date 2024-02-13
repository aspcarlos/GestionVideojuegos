package com.example.gestionvideojuegos

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gestionvideojuegos.database.Videojuegos
import com.example.gestionvideojuegos.databinding.ActivityNuevoVideojuegoBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class AñadirVideojuegoActivity : ActivityWithMenu() {

    private lateinit var binding: ActivityNuevoVideojuegoBinding
    private lateinit var videojuego: Videojuegos
    lateinit var imagen: ImageButton
    lateinit var urlImagen : Uri // Cambiamos a Uri para almacenar la URI de la imagen seleccionada

    // Constantes para el tamaño máximo de la imagen
    private val MAX_WIDTH = 400
    private val MAX_HEIGHT = 400

    // pickMedia maneja el resultado de una actividad lanzada para seleccionar una imagen en la galeria
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        // Devuelve la uri de la imagen seleccionada:
            imagenUri: Uri? ->
        if (imagenUri != null) {
            // Actualiza la URL de la imagen y muestra la imagen en el ImageButton
            urlImagen = imagenUri
            Picasso.get().load(imagenUri).resize(MAX_WIDTH, MAX_HEIGHT).into(imagen)
            Toast.makeText(this, "Imagen añadida correctamente", Toast.LENGTH_SHORT).show()

        }
        else
        {
            Toast.makeText(this, "Error al añadir la imagen", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoVideojuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagen = binding.imageButtonVideojuego

        val button1 = findViewById<Button>(R.id.bGaleria)
        button1.setBackgroundColor(Color.BLUE)

        val button2 = findViewById<Button>(R.id.bCamara)
        button2.setBackgroundColor(Color.BLUE)

        val button3 = findViewById<Button>(R.id.bAnadir)
        button3.setBackgroundColor(Color.BLUE)

        // Cuando pulsemos sobre el imageButton, llamaremos al launcher (pickMedia) para lanzarlo:
        binding.bGaleria.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        fun getImageUriFromBitmap(context: Context, imageBitmap: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, imageBitmap, "imagenes", null)
            return Uri.parse(path)
        }

        // pickPhoto es un lanzador que maneja el resultado de capturar una imagen desde la camara
        val pickPhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Obtiene un objeto bitmap de los extras del resultado y lo muestra en el ImageButton
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                binding.imageButtonVideojuego.setImageBitmap(imageBitmap)
                // Guarda la imagen en Firebase Storage y actualiza la URL en Firestore
                videojuego.InsertarVideojuego(videojuego, getImageUriFromBitmap(applicationContext, imageBitmap))
            }
        }

        // Cuando pulsemos sobre el boton, llamamos al launcher (pickPhoto) para lanzarlos:
        binding.bCamara.setOnClickListener {
            pickPhoto.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        // Boton de insertar Videojuego
        binding.bAnadir.setOnClickListener {

            // Asignamos el contenido de las editText a variables
            val nombreVid = binding.tbNombreVidAnadir.text.toString()
            val descripcionVid = binding.tbDescripcionVidAnadir.text.toString()

            // Comprobamos que los campos no esten vacios
            if(nombreVid.isEmpty() && descripcionVid.isEmpty())
            {
                Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
            else
            {
                videojuego = Videojuegos(nombre = nombreVid, descripcion = descripcionVid, foto = urlImagen.toString() )

                // Registrar Videojuego
                if(!videojuego.InsertarVideojuego(videojuego, urlImagen))
                {
                    Toast.makeText(this, "Videojuego registrado correctamente", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad actual y vuelve a la anterior
                }
                else
                {
                    // limpiar campos
                    binding.tbNombreVidAnadir.setText("")
                    binding.tbDescripcionVidAnadir.setText("")

                    Toast.makeText(this, "Error al registrar el Videojuego", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

