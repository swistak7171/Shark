package pl.kamilszustak.shark.compiler

import com.google.auto.service.AutoService
import pl.kamilszustak.shark.annotations.Repository
import pl.kamilszustak.shark.compiler.util.isAbstractClass
import pl.kamilszustak.shark.compiler.util.isInterface
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
            Repository::class.java.name
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion =
        SourceVersion.latest()

    override fun process(typeElementSet: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        if (roundEnvironment == null) {
            return false
        }

        val repositories = roundEnvironment.getElementsAnnotatedWith(Repository::class.java)

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
        val fileContent = RepositoryGenerator.generate(element)

        val file = File(generatedCodeDirectory, "$className.kt")
        file.writeText(fileContent)
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}