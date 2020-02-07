package pl.kamilszustak.shark.core

class SharedPreferencesProperty<T: Comparable<T>>(
    private val key: String,
    private val defaultValue: T,
    private val sharedPreferencesManager: SharedPreferencesManager
) : Property<T> {

    override var value: T
        get() = sharedPreferencesManager.getValue(key, defaultValue)
        set(value) {
            sharedPreferencesManager.setValue(key, value)
        }

    override val isPresent: Boolean = sharedPreferencesManager.isPresent(key)

    override fun setValueAsync(value: T) {
        sharedPreferencesManager.setValue(key, value, true)
    }

    override fun restoreDefaultValue() {
        sharedPreferencesManager.setValue(key, defaultValue)
    }
}