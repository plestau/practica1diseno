package com.example.a1practica

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ActividadResultados : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_resultados)

        val operacionesResueltas = intent.getIntExtra("operacionesResueltas", 0)
        val preguntasAcertadas = intent.getIntExtra("preguntasAcertadas", 0)
        val preguntasFalladas = intent.getIntExtra("preguntasFalladas", 0)
        val porcentajeAcierto = intent.getDoubleExtra("porcentajeAcierto", 0.0)

        val textOperacionesResueltas = findViewById<TextView>(R.id.textOperacionesResueltas)
        val textPartidasJugadas = findViewById<TextView>(R.id.textPartidasJugadas)
        val textPreguntasAcertadas = findViewById<TextView>(R.id.textPreguntasAcertadas)
        val textPreguntasFalladas = findViewById<TextView>(R.id.textPreguntasFalladas)
        val textPorcentajeAcierto = findViewById<TextView>(R.id.textPorcentajeAcierto)
        val btnRegresar = findViewById<TextView>(R.id.botonVolver)
        val sharedPreferences = getSharedPreferences("Configuracion", Context.MODE_PRIVATE)
        val partidasJugadasGuardadas = sharedPreferences.getInt("partidas_jugadas", 0)

        textOperacionesResueltas.text = "Operaciones resueltas: $operacionesResueltas"
        textPartidasJugadas.text = "Partidas jugadas: $partidasJugadasGuardadas"
        textPreguntasAcertadas.text = "Preguntas acertadas: $preguntasAcertadas"
        textPreguntasFalladas.text = "Preguntas falladas: $preguntasFalladas"
        textPorcentajeAcierto.text = "Porcentaje de acierto: ${String.format("%.2f", porcentajeAcierto)}%"

        btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
