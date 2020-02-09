package pl.kamilszustak.shark.compiler

import com.google.auto.service.AutoService
import pl.kamilszustak.shark.annotations.DefaultValueProvider
import pl.kamilszustak.shark.annotations.Repository
import pl.kamilszustak.shark.compiler.util.isFunction
import pl.kamilszustak.shark.compiler.util.isInterface
import pl.kamilszustak.shark.compiler.util.isPublic
import pl.kamilszustak.shark.compiler.util.isStatic
import java.io.File
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(SharkProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class SharkProcessor : PrintableProcessor() {

    private val store: DefaultValuesStore = DefaultValuesStore()

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            Repository::class.java.name,
            DefaultValueProvider::class.java.name
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion =
        SourceVersion.latest()

    override fun process(typeElementSet: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        if (roundEnvironment == null) {
            return false
        }

        val repositories = roundEnvironment.getElementsAnnotatedWith(Repository::class.java)
        findDefaultValueProviders(roundEnvironment)

        val nameResources = repositories.mapNotNull {
            it.getAnnotation(Repository::class.java)
                ?.nameResource
        }

        if (nameResources.size != nameResources.distinct().size) {
            printError("SharedPreferences cannot have the same name")
            return false
        }

        repositories.forEach { element ->
            if (element == null) {
                printError("Element cannot be null")
                return false
            }

            if (!element.isInterface()) {
                printError("@Repository annotation can be applied only to interfaces")
                return false
            }

            generateRepository(element)
        }

        return true
    }

    private fun generateRepository(element: Element) {
        val className = "${element.simpleName}Impl"
        val packageName = processingEnv.elementUtils.getPackageOf(element).toString()

        val generatedCodeDirectory = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        val fileContent = RepositoryGenerator.generate(element, processingEnv)

        val file = File(generatedCodeDirectory, "$className.kt").apply {
            this.writeText(fileContent)
            this.setReadOnly()
        }
    }

    private fun findDefaultValueProviders(roundEnvironment: RoundEnvironment) {
        val providers = roundEnvironment.getElementsAnnotatedWith(DefaultValueProvider::class.java)
            ?: return

        providers.filterNotNull()
            .forEach { element ->
                if (!(element.isFunction() && element.isStatic() && element.isPublic())) {
                    printError("${DefaultValueProvider.signature} must be a public static function")
                }

                (element as? ExecutableElement)?.let { executableElement ->
                    store.add(executableElement.asType() to executableElement)
                }
            }

        printError(store.getAll().toString())
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}