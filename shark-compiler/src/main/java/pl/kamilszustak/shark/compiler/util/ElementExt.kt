package pl.kamilszustak.shark.compiler.util

import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier

fun Element.isInterface(): Boolean =
    this.kind == ElementKind.INTERFACE

fun Element.isAbstractClass(): Boolean =
    this.kind == ElementKind.CLASS &&
        Modifier.ABSTRACT in this.modifiers

fun Element.isFunction(): Boolean =
    this.kind == ElementKind.METHOD

fun Element.isField(): Boolean =
    this.kind == ElementKind.FIELD

fun Element.isStatic(): Boolean =
    Modifier.STATIC in this.modifiers

fun Element.isPublic(): Boolean =
    Modifier.PUBLIC in this.modifiers