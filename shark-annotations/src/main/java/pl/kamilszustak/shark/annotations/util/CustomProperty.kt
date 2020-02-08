package pl.kamilszustak.shark.annotations.util

import androidx.annotation.StringRes

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomProperty(
    @StringRes
    val keyResource: Int
)