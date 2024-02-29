class Rectangulo(val base: Double, val altura: Double) {
    // 1. Implementar un método llamado 'calcularArea' que calcule y devuelva el área del rectángulo.
    fun calcularArea(): Double {
        return base * altura
    }

    // 2. Implementar un método llamado 'calcularPerimetro' que calcule y devuelva el perímetro del rectángulo.
    fun calcularPerimetro(): Double {
        return 2 * (base + altura)
    }
}

fun main() {
    // Crear un rectángulo con una base de 5.0 y una altura de 3.0
    val miRectangulo = Rectangulo(5.0, 3.0)

    // Imprimir las dimensiones del rectángulo
    println("Dimensiones del rectángulo - Base: ${miRectangulo.base}, Altura: ${miRectangulo.altura}")

    // 3. Llamar a los métodos 'calcularArea' y 'calcularPerimetro' de la clase Rectangulo e imprimir los resultados.
    //    Asegúrate de utilizar println para mostrar los resultados de manera clara.
    val area = miRectangulo.calcularArea()
    val perimetro = miRectangulo.calcularPerimetro()

    println("Área del rectángulo: $area")
    println("Perímetro del rectángulo: $perimetro")
}