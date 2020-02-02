package pl.kamilszustak.shark.compiler

import javax.annotation.processing.AbstractProcessor
import javax.tools.Diagnostic

abstract class PrintableProcessor : AbstractProcessor(), Printable {

    override fun printError(message: String) {
        print(Diagnostic.Kind.ERROR, message)
    }

    override fun printWarning(message: String) {
        print(Diagnostic.Kind.WARNING, message)
    }

    override fun printMandatoryWarning(message: String) {
        print(Diagnostic.Kind.MANDATORY_WARNING, message)
    }

    override fun printNote(message: String) {
        print(Diagnostic.Kind.NOTE, message)
    }

    override fun printOther(message: String) {
        print(Diagnostic.Kind.OTHER, message)
    }

    private fun print(kind: Diagnostic.Kind, message: String) {
        processingEnv?.messager?.printMessage(kind, message)
    }
}