package pl.kamilszustak.shark.core

class SharedPreferencesProperty<T: Comparable<T>>(
    private val key: String,
    private val defaultValue: T,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : Property<T> {

    override var value: T
        get() = sharedPreferencesRepository.getValue(key, defaultValue)
        set(value) {
            sharedPreferencesRepository.setValue(key, value)
        }

    override val isPresent: Boolean = sharedPreferencesRepository.isPresent(key)

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