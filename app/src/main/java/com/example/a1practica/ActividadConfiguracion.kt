package com.example.a1practica

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner

class ActividadConfiguracion : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var editarCuentaAtras: EditText
    private lateinit var minimo: EditText
    private lateinit var maximo: EditText
    private lateinit var suma: CheckBox
    private lateinit var resta: CheckBox
    private lateinit var multiplicacion: CheckBox
    private lateinit var animacion: Spinner
    private lateinit var botonGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        prefs = getSharedPreferences("Configuracion", Context.MODE_PRIVATE)
        editor = prefs.edit()

        editarCuentaAtras = findViewById(R.id.editarCuentaAtras)
        minimo = findViewById(R.id.minimo)
        maximo = findViewById(R.id.maximo)
        suma = findViewById(R.id.suma)
        resta = findViewById(R.id.resta)
        multiplicacion = findViewById(R.id.multiplicacion)
        animacion = findViewById(R.id.animacion)
        botonGuardar = findViewById(R.id.botonGuardar)

        // Cargar configuración por defecto o existente
        cargarConfiguracion()

        botonGuardar.setOnClickListener {
            // Guardar la configuración en SharedPreferences
            guardarConfiguracion()

            // Puedes regresar a la actividad anterior o realizar alguna acción adicional
            finish()
        }
    }

    private fun cargarConfiguracion() {
        val tiempoDefecto = "20"
        val minimoDefecto = "10"
        val maximoDefecto = "20"
        val animacionDefecto = 0
        val sumaDefecto = true
        val restaDefecto = true
        val multiplicacionDefecto = false

        val tiempo = prefs.getString("tiempo", tiempoDefecto) ?: tiempoDefecto
        val min = prefs.getString("minimo", minimoDefecto) ?: minimoDefecto
        val max = prefs.getString("maximo", maximoDefecto) ?: maximoDefecto
        val anim = prefs.getInt("animacion", animacionDefecto)
        val suma = prefs.getBoolean("default_suma", sumaDefecto)
        val resta = prefs.getBoolean("default_resta", restaDefecto)
        val multiplicacion = prefs.getBoolean("default_multiplicacion", multiplicacionDefecto)

        // Actualizar los elementos de la interfaz con los valores cargados
        editarCuentaAtras.setText(tiempo)
        minimo.setText(min)
        maximo.setText(max)
        this.suma.isChecked = suma
        this.resta.isChecked = resta
        this.multiplicacion.isChecked = multiplicacion
        animacion.setSelection(anim)
    }

    private fun guardarConfiguracion() {
        editor.putString("tiempo", editarCuentaAtras.text.toString())
        editor.putString("minimo", minimo.text.toString())
        editor.putString("maximo", maximo.text.toString())
        editor.putBoolean("default_suma", suma.isChecked)
        editor.putBoolean("default_resta", resta.isChecked)
        editor.putBoolean("default_multiplicacion", multiplicacion.isChecked)
        editor.putInt("animacion", animacion.selectedItemPosition)
        editor.apply()

        val intent = Intent(this, ActividadCalculadora::class.java)
        intent.putExtra("tiempo", editarCuentaAtras.text.toString())
        intent.putExtra("minimo", minimo.text.toString())
        intent.putExtra("maximo", maximo.text.toString())
        intent.putExtra("default_suma", suma.isChecked)
        intent.putExtra("default_resta", resta.isChecked)
        intent.putExtra("default_multiplicacion", multiplicacion.isChecked)
        intent.putExtra("animacion", animacion.selectedItemPosition)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
