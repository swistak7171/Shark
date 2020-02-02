package pl.kamilszustak.shark.annotations

import androidx.annotation.StringRes

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BooleanProperty(
    @StringRes
    val keyResource: Int,
    val defaultValue: Boolean = false
)