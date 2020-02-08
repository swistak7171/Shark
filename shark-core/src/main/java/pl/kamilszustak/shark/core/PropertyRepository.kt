package pl.kamilszustak.shark.core

interface PropertyRepository {
    val all: Map<String, *>

    fun restoreDefaultValues()
}