package pl.kamilszustak.shark.compiler

import javax.lang.model.element.Element

data class AnnotatedElement(
    val name: String,
    val annotation: Annotation,
    val element: Element
)