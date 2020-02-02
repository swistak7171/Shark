package pl.kamilszustak.shark.core

import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferencesManager(
    private val sharedPreferences: SharedPreferences
) {

    @Suppress("UNCHECKED_CAST")
    fun <T : Comparable<T>> getValue(
        key: String,
        defaultValue: T
    ): T {
        return try {
            when (defaultValue) {
                is Int -> sharedPreferences.getInt(key, defaultValue)
                is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
                is String -> sharedPreferences.getString(key, defaultValue)
                is Float -> sharedPreferences.getFloat(key, defaultValue)
                is Long -> sharedPreferences.getLong(key, defaultValue)
                else -> defaultValue
            } as T
        } catch (exception: Exception) {
            exception.printStackTrace()
            defaultValue
        }
    }

    fun <T: Comparable<T>> setValue(pair: Pair<String, T>, synchronously: Boolean = true) {
        this.setValue(pair.first, pair.second, synchronously)
    }

    fun <T : Comparable<T>> setValue(
        key: String,
        value: T,
        synchronously: Boolean = false
    ) {
        sharedPreferences.edit(synchronously) {
            when (value) {
                is Int -> this.putInt(key, value)
                is Boolean -> this.putBoolean(key, value)
                is String -> this.putString(key, value)
                is Float -> this.putFloat(key, value)
                is Long -> this.putLong(key, value)
            }
        }
    }

    fun isPresent(key: String): Boolean =
        sharedPreferences.contains(key)
}