fun main() {
    // 1. Solicitar al usuario que ingrese la temperatura actual en grados Celsius.
    print("Ingresa la temperatura actual en grados Celsius: ")
    val temperatura = readLine()?.toDoubleOrNull()

    // 2. Validar si la entrada del usuario es válida (no nula y es un número).
    if (temperatura != null) {
        // 3. Utilizar estructuras condicionales para clasificar la temperatura.
        // 4. Imprimir mensajes según la clasificación.

        when {
            temperatura < 0 -> println("Hace mucho frío. Abrígate bien.")
            temperatura in 0.0..10.0 -> println("Hace frío. Ponte una chaqueta.")
            temperatura in 10.1..20.0 -> println("La temperatura es agradable.")
            temperatura in 20.1..30.0 -> println("Hace calor. Disfruta del clima.")
            else -> {
                println("Hace mucho calor. ¡Cuidado con el sol!")

                // 5. Utilizar una estructura 'if-else' para agregar condiciones adicionales.
                if (temperatura > 30 && temperatura <= 35) {
                    println("Hace calor extremo. Ten precaución.")
                } else if (temperatura > 35) {
                    println("¡Es un día muy caluroso! Evita exponerte al sol durante mucho tiempo.")
                }
            }
        }

        // 6. Solicitar al usuario que ingrese el día de la semana.
        print("Ingresa el día de la semana: ")
        val dia = readLine()

        // 7. Utilizar la estructura 'when' para clasificar el día de la semana.
        when (dia?.toLowerCase()) {
            "lunes", "martes", "miércoles", "jueves", "viernes" -> println("Es un día laboral. ¡Ve al colegio o al trabajo!")
            "sábado", "domingo" -> println("Es fin de semana. ¡Disfruta tu tiempo libre!")
            else -> println("No has ingresado un día válido.")
        }

    } else {
        // 8. En caso de una entrada no válida, imprimir un mensaje de error.
        println("Entrada no válida. Por favor, ingresa un número válido.")
    }
}