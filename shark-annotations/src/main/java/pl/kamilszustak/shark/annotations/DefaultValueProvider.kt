package pl.kamilszustak.shark.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultValueProvider {
    companion object {
        val signature: String = "@${DefaultValueProvider::class.java.simpleName}"
    }
}