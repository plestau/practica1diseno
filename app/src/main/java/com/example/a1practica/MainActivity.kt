package com.example.a1practica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val memoriTron = findViewById<Button>(R.id.ejercicioMemoria)
        val calculadora = findViewById<Button>(R.id.ejercicioCalculadora)

        memoriTron.setOnClickListener{
            val intent = Intent(this, ActividadMemoria::class.java)
            startActivity(intent)
        }

        calculadora.setOnClickListener{
            val intent = Intent(this, ActividadCalculadora::class.java)
            startActivity(intent)
        }
    }
}