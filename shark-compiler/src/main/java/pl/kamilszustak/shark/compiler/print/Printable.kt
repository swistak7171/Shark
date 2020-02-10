package pl.kamilszustak.shark.compiler.print

interface Printable {
    fun error(message: String)
    fun warning(message: String)
    fun mandatoryWarning(message: String)
    fun note(message: String)
    fun other(message: String)
}