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
    protected val application: Application,
    @StringRes
    nameResource: Int,
    isEncrypted: Boolean = false
) : PropertyRepository {

    private val sharedPreferences: SharedPreferences
    protected val sharedPreferencesManager: SharedPreferencesManager

    init {
        sharedPreferences = createSharedPreferences(nameResource, isEncrypted)
        sharedPreferencesManager = SharedPreferencesManager(sharedPreferences)
    }

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

    override fun restoreDefaultValues() {
        sharedPreferences.edit {
            this.clear()
        }
    }

    protected fun getString(@StringRes stringResource: Int): String =
        application.resources.getString(stringResource)
}