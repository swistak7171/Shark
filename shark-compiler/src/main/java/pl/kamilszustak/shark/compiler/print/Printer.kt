package pl.kamilszustak.shark.compiler.print

import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class Printer(
    private val processingEnvironment: ProcessingEnvironment?
) : Printable {

    override fun error(message: String) {
        print(Diagnostic.Kind.ERROR, message)
    }

    override fun warning(message: String) {
        print(Diagnostic.Kind.WARNING, message)
    }

    override fun mandatoryWarning(message: String) {
        print(Diagnostic.Kind.MANDATORY_WARNING, message)
    }

    override fun note(message: String) {
        print(Diagnostic.Kind.NOTE, message)
    }

    override fun other(message: String) {
        print(Diagnostic.Kind.OTHER, message)
    }

    private fun print(kind: Diagnostic.Kind, message: String) {
        processingEnvironment?.messager?.printMessage(kind, message)
    }
}