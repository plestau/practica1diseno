package com.example.a1practica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ActividadMemoria : AppCompatActivity() {
    private var vidas = 5
    private var primeraCartaVolteada: ImageView? = null
    private var segundaCartaVolteada: ImageView? = null
    private var cartasVolteadas = 0
    private var totalCartasVolteadas = 0
    private lateinit var vidasTextView: TextView
    private val cartas = mutableListOf(R.drawable.unoespadas, R.drawable.dosespadas, R.drawable.tresespadas, R.drawable.cuatroespadas, R.drawable.cincoespadas, R.drawable.seisespadas)

    private val indicesCartas = mutableListOf<Int>()
    private val tagsOG = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_memoria)

        val cardImageViews = listOf(
            R.id.card1, R.id.card2, R.id.card3, R.id.card4,
            R.id.card5, R.id.card6, R.id.card7, R.id.card8,
            R.id.card9, R.id.card10, R.id.card11, R.id.card12
        ).map { findViewById<ImageView>(it) }

        // Initialize ImageViews for cards and set onClick listeners
        vidasTextView = findViewById(R.id.vidasText)
        val card1: ImageView = findViewById<ImageView>(R.id.card1)
        val card2: ImageView = findViewById<ImageView>(R.id.card2)
        val card3: ImageView = findViewById<ImageView>(R.id.card3)
        val card4: ImageView = findViewById<ImageView>(R.id.card4)
        val card5: ImageView = findViewById<ImageView>(R.id.card5)
        val card6: ImageView = findViewById<ImageView>(R.id.card6)
        val card7: ImageView = findViewById<ImageView>(R.id.card7)
        val card8: ImageView = findViewById<ImageView>(R.id.card8)
        val card9: ImageView = findViewById<ImageView>(R.id.card9)
        val card10: ImageView = findViewById<ImageView>(R.id.card10)
        val card11: ImageView = findViewById<ImageView>(R.id.card11)
        val card12: ImageView = findViewById<ImageView>(R.id.card12)

        card1.setOnClickListener { onCardClicked(it as ImageView) }
        card2.setOnClickListener { onCardClicked(it as ImageView) }
        card3.setOnClickListener { onCardClicked(it as ImageView) }
        card4.setOnClickListener { onCardClicked(it as ImageView) }
        card5.setOnClickListener { onCardClicked(it as ImageView) }
        card6.setOnClickListener { onCardClicked(it as ImageView) }
        card7.setOnClickListener { onCardClicked(it as ImageView) }
        card8.setOnClickListener { onCardClicked(it as ImageView) }
        card9.setOnClickListener { onCardClicked(it as ImageView) }
        card10.setOnClickListener { onCardClicked(it as ImageView) }
        card11.setOnClickListener { onCardClicked(it as ImageView) }
        card12.setOnClickListener { onCardClicked(it as ImageView) }

        asignarImagenesRandom(cardImageViews)
        saveTagsOG(cardImageViews)
    }

    private fun asignarImagenesRandom(cardImageViews: List<ImageView>) {
        indicesCartas.clear()

        val randomIndices = cartas.shuffled().take(6)
        indicesCartas.addAll(randomIndices)
        indicesCartas.addAll(randomIndices)

        for ((index, card) in cardImageViews.withIndex()) {
            card.setImageResource(R.drawable.reverso_carta)
            card.tag = index
        }
    }

    fun onCardClicked(cartaVolteada: ImageView) {
        if (cartasVolteadas < 2 && cartaVolteada.tag != "flipped") {
            cartasVolteadas++
            if (primeraCartaVolteada == null) {
                primeraCartaVolteada = cartaVolteada
                volteaCartas(cartaVolteada, indicesCartas[cartaVolteada.tag as Int])
            } else if (segundaCartaVolteada == null && primeraCartaVolteada != cartaVolteada) {
                segundaCartaVolteada = cartaVolteada
                volteaCartas(cartaVolteada, indicesCartas[cartaVolteada.tag as Int])

                // Verifica si las cartas coinciden
                if (cardsMatch(primeraCartaVolteada!!, segundaCartaVolteada!!)) {
                    totalCartasVolteadas += 2
                    primeraCartaVolteada!!.tag = "flipped" // Marca las cartas como volteadas
                    segundaCartaVolteada!!.tag = "flipped"
                    primeraCartaVolteada = null
                    segundaCartaVolteada = null
                    cartasVolteadas = 0
                    checkGameEnd() // Comprueba si todas las cartas han sido volteadas
                } else {
                    Handler().postDelayed({
                        volteaCartas(primeraCartaVolteada!!, R.drawable.reverso_carta)
                        volteaCartas(segundaCartaVolteada!!, R.drawable.reverso_carta)
                        primeraCartaVolteada = null
                        segundaCartaVolteada = null
                        cartasVolteadas = 0
                        updateVidas(-1) // Resta una vida
                    }, 1000)
                }
            }
        }
    }

    private fun saveTagsOG(cardImageViews: List<ImageView>) {
        tagsOG.clear()
        // Guarda los índices originales en la lista
        for ((index, _) in cardImageViews.withIndex()) {
            tagsOG.add(index)
        }
    }

    private fun volteaCartas(card: ImageView, drawableResource: Int, delay: Long = 0L) {
        Handler().postDelayed({
            card.setImageResource(drawableResource)
            if (delay > 0) {
                Handler().postDelayed({
                    card.setImageResource(drawableResource)
                }, delay)
            }
        }, delay)
    }

    private fun cardsMatch(card1: ImageView, card2: ImageView): Boolean {
        val match = indicesCartas[card1.tag as Int] == indicesCartas[card2.tag as Int]
        if (match) {
            card1.isClickable = false
            card2.isClickable = false
            totalCartasVolteadas += 2
            checkGameEnd() // Verificar si todas las cartas han sido volteadas
        } else {
            volteaCartas(card1, R.drawable.reverso_carta, 1000)
            volteaCartas(card2, R.drawable.reverso_carta, 1000)
        }
        return match
    }


    private fun updateVidasUI() {
        vidasTextView.text = "Vidas restantes: $vidas"
    }

    private fun updateVidas(amount: Int) {
        vidas += amount
        updateVidasUI()
        if (vidas <= 0) {
            gameOver()
        }
    }

    private fun gameOver() {
        val restartButton: Button = findViewById(R.id.restartButton)
        restartButton.visibility = View.VISIBLE

        // Deshabilitar todas las cartas
        val todasLasCartaws = listOf(
            findViewById<ImageView>(R.id.card1),
            findViewById<ImageView>(R.id.card2),
            findViewById<ImageView>(R.id.card3),
            findViewById<ImageView>(R.id.card4),
            findViewById<ImageView>(R.id.card5),
            findViewById<ImageView>(R.id.card6),
            findViewById<ImageView>(R.id.card7),
            findViewById<ImageView>(R.id.card8),
            findViewById<ImageView>(R.id.card9),
            findViewById<ImageView>(R.id.card10),
            findViewById<ImageView>(R.id.card11),
            findViewById<ImageView>(R.id.card12)
        )
        todasLasCartaws.forEach { card ->
            card.isClickable = false
        }
        if(totalCartasVolteadas == (indicesCartas.size * 2)) {
            vidasTextView.text = "¡Ganaste!"
        }else{
            vidasTextView.text = "Game Over"
        }
    }

    fun restartGame(view: View) {
        val restartButton: Button = findViewById(R.id.restartButton)
        restartButton.visibility = View.GONE

        vidas = 5
        vidasTextView.text = "Vidas restantes: $vidas"

        //habilitar todas las cartas
        val todasLasCartaws = listOf(
            findViewById<ImageView>(R.id.card1),
            findViewById<ImageView>(R.id.card2),
            findViewById<ImageView>(R.id.card3),
            findViewById<ImageView>(R.id.card4),
            findViewById<ImageView>(R.id.card5),
            findViewById<ImageView>(R.id.card6),
            findViewById<ImageView>(R.id.card7),
            findViewById<ImageView>(R.id.card8),
            findViewById<ImageView>(R.id.card9),
            findViewById<ImageView>(R.id.card10),
            findViewById<ImageView>(R.id.card11),
            findViewById<ImageView>(R.id.card12)
        )
        todasLasCartaws.forEachIndexed { index, card ->
            card.isClickable = true
            card.tag = tagsOG[index]
        }
        cartasVolteadas = 0
        totalCartasVolteadas = 0
        asignarImagenesRandom(todasLasCartaws)
    }

    private fun checkGameEnd() {
        if (totalCartasVolteadas == (indicesCartas.size * 2)) {
            gameOver()
        }
    }
}