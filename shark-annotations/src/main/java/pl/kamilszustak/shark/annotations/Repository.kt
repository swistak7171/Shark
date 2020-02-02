package pl.kamilszustak.shark.annotations

import androidx.annotation.StringRes

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Repository(
    @StringRes
    val nameResource: Int,
    val isEncrypted: Boolean = false
)