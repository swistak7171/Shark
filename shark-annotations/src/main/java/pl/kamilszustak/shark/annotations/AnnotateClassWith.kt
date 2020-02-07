package pl.kamilszustak.shark.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AnnotateClassWith(
    vararg val annotation: KClass<out Annotation>
)