fun main() {
    // Crear una lista de números
    val numeros = mutableListOf(5, 2, 8, 1, 7)

    // Imprimir la lista original
    println("Lista original: $numeros")

    // 1. Crear una función que calcule y devuelva la suma de todos los números en la lista
    val suma = calcularSuma(numeros)
    println("La suma de los números en la lista es: $suma")

    // 2. Crear una función que encuentre y devuelva el número más grande en la lista
    val maximo = encontrarMaximo(numeros)
    println("El número más grande en la lista es: $maximo")

    // 3. Crear una función que ordene la lista de manera ascendente y la imprima
    ordenarAscendente(numeros)
}

// Función para calcular la suma de una lista de números
fun calcularSuma(lista: List<Int>): Int {
    return lista.sum()
}

// Función para encontrar el número más grande en una lista de números
fun encontrarMaximo(lista: List<Int>): Int? {
    return lista.maxOrNull()
}

// Función para ordenar una lista de manera ascendente y luego imprimir la lista
fun ordenarAscendente(lista: MutableList<Int>) {
    lista.sort()
    println("Lista ordenada de manera ascendente: $lista")
}