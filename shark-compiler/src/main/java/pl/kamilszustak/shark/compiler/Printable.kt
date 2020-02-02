package pl.kamilszustak.shark.compiler

interface Printable {
    fun printError(message: String)
    fun printWarning(message: String)
    fun printMandatoryWarning(message: String)
    fun printNote(message: String)
    fun printOther(message: String)
}