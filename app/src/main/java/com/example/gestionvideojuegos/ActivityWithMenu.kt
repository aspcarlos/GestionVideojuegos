package com.example.gestionvideojuegos

import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

open class ActivityWithMenu: AppCompatActivity() {
    companion object {
        var actividadActual = 1;
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Relacionamos la clase con el layout del menú que hemos creado:
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.listado_videojuegos ->  {
                actividadActual = 1
                val intent = Intent(this, ListadoActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
                true
            }

            R.id.anadir_videojuego -> {
                actividadActual= 2
                val intent = Intent(this, AñadirVideojuegoActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
                true
            }

            R.id.eliminar_videojuego -> {
                actividadActual = 3
                val intent = Intent(this, EliminarVideojuegoActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}