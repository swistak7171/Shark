package pl.kamilszustak.shark.core

class SharedPreferencesProperty<T: Comparable<T>>(
    private val key: String,
    private val defaultValue: T,
    private val sharedPreferencesManager: SharedPreferencesManager
) {

    var value: T
        get() = sharedPreferencesManager.getValue(key, defaultValue)
        set(value) {
            sharedPreferencesManager.setValue(key, value)
        }

    val isPresent: Boolean = sharedPreferencesManager.isPresent(key)

    fun setValueAsync(value: T) {
        sharedPreferencesManager.setValue(key, value, true)
    }

    fun setDefaultValue() {
        sharedPreferencesManager.setValue(key, defaultValue)
    }
}