package pl.kamilszustak.shark.annotations.util

import pl.kamilszustak.shark.annotations.BooleanProperty
import pl.kamilszustak.shark.annotations.FloatProperty
import pl.kamilszustak.shark.annotations.IntProperty
import pl.kamilszustak.shark.annotations.LongProperty
import pl.kamilszustak.shark.annotations.StringProperty

object AnnotationHelper {

    val propertyAnnotations: List<Class<out Annotation>> = listOf(
        BooleanProperty::class.java,
        FloatProperty::class.java,
        IntProperty::class.java,
        LongProperty::class.java,
        StringProperty::class.java,
        CustomProperty::class.java
    )
}