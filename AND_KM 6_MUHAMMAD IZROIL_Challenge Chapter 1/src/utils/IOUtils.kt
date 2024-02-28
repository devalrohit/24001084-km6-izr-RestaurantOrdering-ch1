package app.utils

object IOUtils {
    fun getInputInteger(): Int? {
        val input = readLine()
        return if (input != null) {
            try {
                input.toInt()
            } catch (e: NumberFormatException) {
                println("Input harus berupa angka")
                null
            }
        } else {
            null
        }
    }
}
