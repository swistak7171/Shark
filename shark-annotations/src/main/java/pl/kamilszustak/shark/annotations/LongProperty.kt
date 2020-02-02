package pl.kamilszustak.shark.annotations

import androidx.annotation.StringRes

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LongProperty(
    @StringRes
    val keyResource: Int,
    val defaultValue: Long = 0L
)