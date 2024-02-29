import kotlin.random.Random

class JuegoAdivinanzas(private val numeroSecreto: Int) {
    private val intentosHistorial = mutableListOf<Int>()

    fun adivinarNumero(numero: Int): String {
        intentosHistorial.add(numero)
        return when {
            numero == numeroSecreto -> "¡Felicidades! Adivinaste el número secreto $numeroSecreto en ${intentosHistorial.size} intentos."
            numero < numeroSecreto -> "El número secreto es mayor. Intenta nuevamente."
            else -> "El número secreto es menor. Intenta nuevamente."
        }
    }

    fun obtenerIntentosHistorial(): List<Int> = intentosHistorial.toList()
}

fun main() {
    val numeroSecreto = Random.nextInt(1, 11)
    val juego = JuegoAdivinanzas(numeroSecreto)

    println("¡Bienvenido al Juego de Adivinanzas!")

    while (true) {
        print("Ingresa un número entre 1 y 10: ")
        val input = readLine()

        if (input != null) {
            val adivinanza = input.toIntOrNull()

            if (adivinanza != null) {
                val resultado = juego.adivinarNumero(adivinanza)
                println(resultado)

                if (adivinanza == numeroSecreto) {
                    println("Historial de intentos: ${juego.obtenerIntentosHistorial()}")
                    break
                }
            } else {
                println("Por favor, ingresa un número válido.")
            }
        } else {
            println("Por favor, ingresa un número.")
        }
    }
}