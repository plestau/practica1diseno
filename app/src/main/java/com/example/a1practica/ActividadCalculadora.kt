package com.example.a1practica

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat

class ActividadCalculadora : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private var tiempoInicial = 0L
    private var duracionCuenta = 20 * 1000L
    private var cuentaAtras: CountDownTimer? = null
    private lateinit var temporizador : Chronometer
    private lateinit var numeroAcertadas : TextView
    private lateinit var numeroFalladas : TextView
    private lateinit var operacionPasada : TextView
    private lateinit var resultadoOperacionAnterior : ImageView
    private lateinit var operacionActual : TextView
    private lateinit var operacionSiguiente : TextView
    private lateinit var configuracion : ImageView
    private lateinit var boton0 : Button
    private lateinit var boton1 : Button
    private lateinit var boton2 : Button
    private lateinit var boton3 : Button
    private lateinit var boton4 : Button
    private lateinit var boton5 : Button
    private lateinit var boton6 : Button
    private lateinit var boton7 : Button
    private lateinit var boton8 : Button
    private lateinit var boton9 : Button
    private lateinit var botonC : Button
    private lateinit var botonCE : Button
    private lateinit var botonResultado : Button
    private lateinit var inputCalculadora: TextView

    private var operacionAnterior = ""
    private var operacionActualStr = ""
    private var operacionSiguienteStr = ""
    private var contadorAcertadas = 0
    private var contadorFalladas = 0

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_calculadora)
        sharedPreferences = getSharedPreferences("Configuracion", Context.MODE_PRIVATE)
        val tiempoPorDefecto = resources.getString(R.string.contador)
        val minimoPorDefecto = resources.getString(R.string.minimo)
        val maximoPorDefecto = resources.getString(R.string.maximo)
        val sumaPorDefecto = resources.getBoolean(R.bool.default_suma)
        val restaPorDefecto = resources.getBoolean(R.bool.default_resta)
        val multiplicacionPorDefecto = resources.getBoolean(R.bool.default_multiplicacion)
        val animacionPorDefecto = resources.getInteger(R.integer.default_animacion)
        val partidasJugadas = sharedPreferences.getInt("partidas_jugadas", 0) + 1

        // Guardar los valores por defecto en SharedPreferences si no existen
        val editor = sharedPreferences.edit()
        editor.putString("tiempo", tiempoPorDefecto)
        editor.putString("minimo", minimoPorDefecto)
        editor.putString("maximo", maximoPorDefecto)
        editor.putInt("partidas_jugadas", partidasJugadas)
        editor.putInt("animacion", animacionPorDefecto)
        editor.putBoolean("default_suma", sumaPorDefecto)
        editor.putBoolean("default_resta", restaPorDefecto)
        editor.putBoolean("default_multiplicacion", multiplicacionPorDefecto)
        // Asegúrate de aplicar el guardado
        editor.apply()

        // actualiza el temporizador si la configuración ha sido actualizada
        temporizador = findViewById<Chronometer>(R.id.temporizador)
        numeroAcertadas = findViewById<TextView>(R.id.numeroAcertadas)
        numeroFalladas = findViewById<TextView>(R.id.numeroFalladas)
        operacionPasada = findViewById<TextView>(R.id.operacionPasada)
        resultadoOperacionAnterior = findViewById<ImageView>(R.id.resultadoOperacionAnterior)
        operacionActual = findViewById<TextView>(R.id.operacionActual)
        operacionSiguiente = findViewById<TextView>(R.id.operacionSiguiente)
        configuracion = findViewById<ImageView>(R.id.configuracion)
        boton0 = findViewById<Button>(R.id.boton0)
        boton1 = findViewById<Button>(R.id.boton1)
        boton2 = findViewById<Button>(R.id.boton2)
        boton3 = findViewById<Button>(R.id.boton3)
        boton4 = findViewById<Button>(R.id.boton4)
        boton5 = findViewById<Button>(R.id.boton5)
        boton6 = findViewById<Button>(R.id.boton6)
        boton7 = findViewById<Button>(R.id.boton7)
        boton8 = findViewById<Button>(R.id.boton8)
        boton9 = findViewById<Button>(R.id.boton9)
        botonC = findViewById<Button>(R.id.botonBorrarTodo)
        botonCE = findViewById<Button>(R.id.botonBorrar)
        botonResultado = findViewById<Button>(R.id.botonResultado)
        inputCalculadora = findViewById<TextView>(R.id.inputCalculadora)

        operacionAnterior = ""
        operacionActualStr = ""
        operacionSiguienteStr = ""

        configuracion.setOnClickListener {
            val intent = Intent(this, ActividadConfiguracion::class.java)
            ActivityCompat.startActivityForResult(this, intent, 1, null)
        }
        boton0.setOnClickListener {
            anadirNumero(0)
        }
        boton1.setOnClickListener {
            anadirNumero(1)
        }
        boton2.setOnClickListener {
            anadirNumero(2)
        }
        boton3.setOnClickListener {
            anadirNumero(3)
        }
        boton4.setOnClickListener {
            anadirNumero(4)
        }
        boton5.setOnClickListener {
            anadirNumero(5)
        }
        boton6.setOnClickListener {
            anadirNumero(6)
        }
        boton7.setOnClickListener {
            anadirNumero(7)
        }
        boton8.setOnClickListener {
            anadirNumero(8)
        }
        boton9.setOnClickListener {
            anadirNumero(9)
        }
        botonC.setOnClickListener {
            borrarTodo()
        }
        botonCE.setOnClickListener {
            borrarUltimoDigito()
        }
        botonResultado.setOnClickListener {
            verificarOperacion()
        }

        iniciarCuentaAtras()
        generarOperacion(false)
        actualizarConfiguracion()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

        temporizador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitamos hacer nada antes del cambio de texto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                actualizarAnimacionContador()
            }

            override fun afterTextChanged(s: Editable?) {
                // No necesitamos hacer nada después del cambio de texto
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        // Detectar cambios en la configuración y actualizar la interfaz aquí
        if (key == "tiempo" || key == "minimo" || key == "maximo" || key == "default_suma" || key == "default_resta" || key == "default_multiplicacion" || key == "animacion") {
            // Actualizar la configuración en tu actividad
            actualizarConfiguracion()
        }
    }

    private fun actualizarConfiguracion() {
        duracionCuenta = sharedPreferences.getString("tiempo", "20")!!.toLong() * 1000L
        // Actualizar el temporizador
        val tiempoTranscurrido = SystemClock.elapsedRealtime() - tiempoInicial
        val tiempoRestante = duracionCuenta - tiempoTranscurrido
        temporizador.base = SystemClock.elapsedRealtime() + tiempoRestante

        // Actualizar el número de operaciones acertadas y falladas
        numeroAcertadas.text = contadorAcertadas.toString()
        numeroFalladas.text = contadorFalladas.toString()

        // Actualizar la operación pasada
        operacionPasada.text = operacionAnterior

        // Actualizar la operación actual
        operacionActual.text = operacionActualStr

        // Actualizar la operación siguiente
        operacionSiguiente.text = operacionSiguienteStr

        tiempoInicial = System.currentTimeMillis()
        cuentaAtras?.cancel()
        iniciarCuentaAtras()
    }

    private fun actualizarAnimacionContador() {
        val animacionSeleccionada = sharedPreferences.getInt("animacion", 0)
        val contador = findViewById<TextView>(R.id.temporizador)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        // Aplicar la animación al TextView del contador según la selección
        when (animacionSeleccionada) {
            0 -> {} // Sin animación, puedes manejarlo de acuerdo a tu lógica
            1 -> {
                contador.startAnimation(fadeIn)
            }
            2 -> {
                contador.startAnimation(fadeOut)
            }
            // Puedes agregar más casos según tus necesidades para otras animaciones
            else -> {}
        }
    }

    private fun iniciarCuentaAtras(){
        cuentaAtras = object : CountDownTimer(duracionCuenta, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                temporizador.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                // Lógica para dirigirse a la actividad de resultados cuando el contador llega a cero
                val intent = Intent(this@ActividadCalculadora, ActividadResultados::class.java)
                intent.putExtra("operacionesResueltas", contadorAcertadas + contadorFalladas)
                intent.putExtra("partidasJugadas", contadorAcertadas + contadorFalladas)
                intent.putExtra("preguntasAcertadas", contadorAcertadas)
                intent.putExtra("preguntasFalladas", contadorFalladas)
                if (contadorAcertadas + contadorFalladas > 0) {
                    val porcentajeAcierto = (contadorAcertadas.toDouble() / (contadorAcertadas + contadorFalladas)) * 100
                    intent.putExtra("porcentajeAcierto", porcentajeAcierto)
                } else {
                    intent.putExtra("porcentajeAcierto", 0.0)
                }
                startActivity(intent)
            }
        }
        cuentaAtras?.start()
    }

    private fun generarOperacion(esSiguiente: Boolean = false) {
        var numero1: Int
        var numero2: Int
        var operacion: Int
        var resultado: Int
        val minimo = sharedPreferences.getString("minimo", "1")?.toInt() ?: 1
        val maximo = sharedPreferences.getString("maximo", "100")?.toInt() ?: 100

        val sumaPermitida = sharedPreferences.getBoolean("default_suma", true)
        val restaPermitida = sharedPreferences.getBoolean("default_resta", true)
        val multiplicacionPermitida = sharedPreferences.getBoolean("default_multiplicacion", true)

        val operacionesPermitidas = mutableListOf<Int>()

        if (sumaPermitida) {
            operacionesPermitidas.add(0)
        }
        if (restaPermitida) {
            operacionesPermitidas.add(1)
        }
        if (multiplicacionPermitida) {
            operacionesPermitidas.add(2)
        }

        if (operacionesPermitidas.isNotEmpty()) {
            operacion = operacionesPermitidas.random()

            do {
                numero1 = (minimo..maximo).random()
                numero2 = (minimo..maximo).random()

                resultado = when (operacion) {
                    0 -> numero1 + numero2
                    1 -> numero1 - numero2
                    else -> numero1 * numero2
                }
            } while (resultado < 0 || (operacion == 2 && numero1 % numero2 != 0))

            val simboloOperacion = when (operacion) {
                0 -> "+"
                1 -> "-"
                else -> "*"
            }

            val operacionStr = "$numero1 $simboloOperacion $numero2 = "
            if (esSiguiente) {
                operacionSiguienteStr = operacionStr
                operacionSiguiente.text = operacionSiguienteStr
            } else {
                operacionActualStr = operacionStr
                operacionActual.text = operacionActualStr
                generarOperacion(true)
            }
        } else {
            if (esSiguiente) {
                operacionSiguienteStr = "No hay operaciones permitidas"
                operacionSiguiente.text = operacionSiguienteStr
            } else {
                operacionActualStr = "No hay operaciones permitidas"
                operacionActual.text = operacionActualStr
                operacionSiguiente.text = ""
            }
        }
    }


    private fun anadirNumero(numero: Int) {
        inputCalculadora.text = inputCalculadora.text.toString() + numero.toString()
    }

    private fun borrarUltimoDigito() {
        if(inputCalculadora.text.isNotEmpty())
            inputCalculadora.text = inputCalculadora.text.substring(0, inputCalculadora.text.length - 1)
    }

    private fun borrarTodo() {
        inputCalculadora.text = ""
    }

    private fun verificarOperacion() {
        if (inputCalculadora.text.isNotEmpty()) {
            val resultado = inputCalculadora.text.toString().toInt()
            // calcular el resultado de la operación actual
            val operacionActual = operacionActualStr.substring(0, operacionActualStr.length - 3)
            val operacionActualSplit = operacionActual.split(" ")
            val numero1 = operacionActualSplit[0].toInt()
            val numero2 = operacionActualSplit[2].toInt()
            val simboloOperacion = operacionActualSplit[1]
            val resultadoOperacionActual = when (simboloOperacion) {
                "+" -> numero1 + numero2
                "-" -> numero1 - numero2
                else -> numero1 * numero2
            }

            // verificar si el resultado es correcto
            if (resultado == resultadoOperacionActual) {
                resultadoOperacionAnterior.setImageResource(R.drawable.baseline_check_24)
                contadorAcertadas++
                numeroAcertadas.text = contadorAcertadas.toString()
            } else {
                resultadoOperacionAnterior.setImageResource(R.drawable.baseline_cancel_24)
                contadorFalladas++
                numeroFalladas.text = contadorFalladas.toString()
            }

            // guardar la operación actual como operación pasada
            operacionPasada.text = operacionActualStr + resultadoOperacionActual.toString()
            operacionAnterior = operacionActualStr + resultadoOperacionActual.toString()
            inputCalculadora.text = ""

            // mover la operación siguiente a la actual
            moverOperacionActual()

            // generar la operación siguiente
            generarOperacion(true)
        }
    }

    private fun moverOperacionActual() {
        operacionActualStr = operacionSiguienteStr
        operacionActual.text = operacionActualStr
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1) && (resultCode == Activity.RESULT_OK)) {
            contadorAcertadas = 0
            contadorFalladas = 0
            numeroAcertadas.text = contadorAcertadas.toString()
            numeroFalladas.text = contadorFalladas.toString()
            operacionAnterior = ""
            resultadoOperacionAnterior.setImageResource(0)

            // Actualizar la configuración en la actividad
            actualizarConfiguracion()
            generarOperacion()
            generarOperacion(true)
            actualizarAnimacionContador()
        }
    }
}