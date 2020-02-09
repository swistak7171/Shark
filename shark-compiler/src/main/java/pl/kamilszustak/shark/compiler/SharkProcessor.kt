package pl.kamilszustak.shark.compiler

import com.google.auto.service.AutoService
import pl.kamilszustak.shark.annotations.AnnotateClassWith
import pl.kamilszustak.shark.annotations.AnnotateConstructorWith
import pl.kamilszustak.shark.annotations.CustomTypeProvider
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
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(SharkProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class SharkProcessor : PrintableProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            Repository::class.java.name,
            CustomTypeProvider::class.java.name
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion =
        SourceVersion.latest()

    override fun process(typeElementSet: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        if (roundEnvironment == null) {
            return false
        }

        val repositories = roundEnvironment.getElementsAnnotatedWith(Repository::class.java)
        getCustomTypeProviders(roundEnvironment)

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

    private fun getCustomTypeProviders(roundEnvironment: RoundEnvironment) {
        val providers = roundEnvironment.getElementsAnnotatedWith(CustomTypeProvider::class.java)
            ?: return

        providers.filterNotNull()
            .forEach loop@{ element ->
                if (!(element.isFunction() && element.isStatic() && element.isPublic())) {
                    printError("${CustomTypeProvider.signature} must be a public static function")
                }
            }
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}