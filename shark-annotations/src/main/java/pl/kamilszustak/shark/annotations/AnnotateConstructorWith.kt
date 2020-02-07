package pl.kamilszustak.shark.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AnnotateConstructorWith(
    vararg val annotation: KClass<out Annotation>
)