package pl.kamilszustak.shark.core

interface Property<T> {
    var value: T
    val isPresent: Boolean

    fun setValueAsync(value: T)
    fun restoreDefaultValue()
    fun restoreDefaultValueAsync()
    fun remove()
    fun removeAsync()
}