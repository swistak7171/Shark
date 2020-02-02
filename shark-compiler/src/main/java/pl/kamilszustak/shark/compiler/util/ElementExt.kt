package pl.kamilszustak.shark.compiler.util

import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier

fun Element.isInterface(): Boolean =
    this.kind == ElementKind.INTERFACE

fun Element.isAbstractClass(): Boolean =
    this.kind == ElementKind.CLASS &&
        Modifier.ABSTRACT in this.modifiers