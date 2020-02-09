package pl.kamilszustak.shark.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomTypeProvider {
    companion object {
        val signature: String = "@${CustomTypeProvider::class.java.simpleName}"
    }
}