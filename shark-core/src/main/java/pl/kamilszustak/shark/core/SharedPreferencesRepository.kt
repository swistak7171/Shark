package pl.kamilszustak.shark.core

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

@Suppress("UNCHECKED_CAST")
abstract class SharedPreferencesRepository(
    private val application: Application,
    @StringRes
    nameResource: Int,
    isEncrypted: Boolean = false
) : PropertyRepository {

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = createSharedPreferences(nameResource, isEncrypted)
    }

    override val all: Map<String, *> = sharedPreferences.all

    private fun createSharedPreferences(nameResource: Int, isEncrypted: Boolean): SharedPreferences {
        val name = getString(nameResource)

        return if (isEncrypted) {
            getEncryptedSharedPreferences(name)
        } else {
            getSharedPreferences(name)
        }
    }

    private fun getSharedPreferences(name: String): SharedPreferences {
        return application.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    private fun getEncryptedSharedPreferences(name: String): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            name,
            masterKeyAlias,
            application.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    protected fun getString(@StringRes stringResource: Int): String =
        application.resources.getString(stringResource)

    @Suppress("UNCHECKED_CAST")
    internal fun <T : Comparable<T>> getValue(
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

    internal fun <T: Comparable<T>> setValue(pair: Pair<String, T>, asynchronously: Boolean = false) {
        this.setValue(pair.first, pair.second, asynchronously)
    }

    internal fun <T : Comparable<T>> setValue(
        key: String,
        value: T,
        asynchronously: Boolean = false
    ) {
        sharedPreferences.edit(!asynchronously) {
            when (value) {
                is Int -> this.putInt(key, value)
                is Boolean -> this.putBoolean(key, value)
                is String -> this.putString(key, value)
                is Float -> this.putFloat(key, value)
                is Long -> this.putLong(key, value)
            }
        }
    }

    internal fun isPresent(key: String): Boolean =
        sharedPreferences.contains(key)

    internal fun remove(key: String, asynchronously: Boolean = false) {
        sharedPreferences.edit(!asynchronously) {
            this.remove(key)
        }
    }

    override fun removeAll() {
        sharedPreferences.edit(true) {
            this.clear()
        }
    }

    override fun removeAllAsync() {
        sharedPreferences.edit {
            this.clear()
        }
    }
}