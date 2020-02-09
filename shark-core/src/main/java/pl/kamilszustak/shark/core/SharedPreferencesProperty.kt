package pl.kamilszustak.shark.core

abstract class SharedPreferencesProperty<T: Comparable<T>>(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : Property<T> {

    abstract val key: String
    abstract val defaultValue: T

    override var value: T
        get() = sharedPreferencesRepository.getValue(key, defaultValue)
        set(value) {
            sharedPreferencesRepository.setValue(key, value)
        }

    override val isPresent: Boolean
        get() = sharedPreferencesRepository.isPresent(key)

    override fun setValueAsync(value: T) {
        sharedPreferencesRepository.setValue(key, value, true)
    }

    override fun restoreDefaultValue() {
        sharedPreferencesRepository.setValue(key, defaultValue)
    }

    override fun restoreDefaultValueAsync() {
        sharedPreferencesRepository.setValue(key, defaultValue, true)
    }

    override fun remove() {
        sharedPreferencesRepository.remove(key)
    }

    override fun removeAsync() {
        sharedPreferencesRepository.remove(key, true)
    }
}