package pl.kamilszustak.shark.compiler

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import pl.kamilszustak.shark.annotations.AnnotateClassWith
import pl.kamilszustak.shark.annotations.AnnotateConstructorWith
import pl.kamilszustak.shark.annotations.BooleanProperty
import pl.kamilszustak.shark.annotations.FloatProperty
import pl.kamilszustak.shark.annotations.IntProperty
import pl.kamilszustak.shark.annotations.LongProperty
import pl.kamilszustak.shark.annotations.Repository
import pl.kamilszustak.shark.annotations.StringProperty
import pl.kamilszustak.shark.annotations.util.AnnotationHelper
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

object RepositoryGenerator {

    private const val MAIN_SHARK_PACKAGE = "pl.kamilszustak.shark"
    private const val CORE_PACKAGE = "$MAIN_SHARK_PACKAGE.core"
    private const val APPLICATION_PACKAGE = "android.app"

    private const val PROPERTY_TYPE = "Property"
    private const val SHARED_PREFERENCES_PROPERTY_TYPE = "SharedPreferencesProperty"
    private const val SHARED_PREFERENCES_REPOSITORY_TYPE = "SharedPreferencesRepository"
    private const val SHARED_PREFERENCES_MANAGER_TYPE = "SharedPreferencesManager"
    private const val APPLICATION_TYPE = "Application"

    private const val APPLICATION_PARAMETER_NAME = "application"
    private const val SHARED_PREFERENCES_NAME = "sharedPreferences"
    private const val SHARED_PREFERENCES_MANAGER_NAME = "sharedPreferencesManager"

    fun generate(element: Element, processingEnvironment: ProcessingEnvironment): String {
        val repositoryAnnotation = element.getAnnotation(Repository::class.java)
            ?: throw IllegalStateException("Class is not annotated with @Repository annotation")

        val className = "${element.simpleName}Impl"

        val file = FileSpec.builder("", className)
            .addImport(CORE_PACKAGE, PROPERTY_TYPE)
            .addImport(CORE_PACKAGE, SHARED_PREFERENCES_PROPERTY_TYPE)
            .addImport(CORE_PACKAGE, SHARED_PREFERENCES_REPOSITORY_TYPE)
            .addImport(APPLICATION_PACKAGE, APPLICATION_TYPE)


        val parentClassType = TypeVariableName(SHARED_PREFERENCES_REPOSITORY_TYPE)

        val parentInterfaceType = TypeVariableName(element.toString())

        val applicationType = TypeVariableName(APPLICATION_TYPE)

        val constructor = FunSpec.constructorBuilder()
            .addParameter(APPLICATION_PARAMETER_NAME, applicationType)

        val constructorAnnotations = getAnnotations<AnnotateConstructorWith>(element, processingEnvironment)
        constructorAnnotations.forEach { className ->
            constructor.addAnnotation(className)
        }

        val repositoryClass = TypeSpec.classBuilder(className)
            .primaryConstructor(constructor.build())
            .superclass(parentClassType)
            .addSuperinterface(parentInterfaceType)
            .addSuperclassConstructorParameter("%N", APPLICATION_PARAMETER_NAME)
            .addSuperclassConstructorParameter("%L", repositoryAnnotation.nameResource)
            .addSuperclassConstructorParameter("%L", repositoryAnnotation.isEncrypted)

        val classAnnotations = getAnnotations<AnnotateClassWith>(element, processingEnvironment)
        classAnnotations.forEach { className ->
            repositoryClass.addAnnotation(className)
        }

        val elements = element.findAnnotatedElements()
        elements.forEach { annotatedElement ->
            val propertyClass = ClassName("", PROPERTY_TYPE)
                .parameterizedBy(getTypeOfProperty(annotatedElement.annotation))

            val annotation = annotatedElement.annotation
            val (keyResource, defaultValue) = when (annotation) {
                is BooleanProperty -> annotation.keyResource to annotation.defaultValue
                is FloatProperty -> annotation.keyResource to annotation.defaultValue
                is IntProperty -> annotation.keyResource to annotation.defaultValue
                is LongProperty -> annotation.keyResource to annotation.defaultValue
                is StringProperty -> annotation.keyResource to annotation.defaultValue
                else -> throw IllegalStateException("Invalid property annotation")
            }

            val initializerString = getPropertyInitializerString(defaultValue)

            val property = PropertySpec.builder(
                annotatedElement.name,
                propertyClass
            )
                .addModifiers(KModifier.OVERRIDE)
                .initializer(
                    initializerString,
                    SHARED_PREFERENCES_PROPERTY_TYPE,
                    keyResource,
                    defaultValue,
                    SHARED_PREFERENCES_MANAGER_NAME
                )
                .build()
            repositoryClass.addProperty(property)
        }

        file.addType(repositoryClass.build())

        return file.build().toString()
    }

    private inline fun <reified T : Annotation> getAnnotations(element: Element, processingEnvironment: ProcessingEnvironment): List<ClassName> {
        val annotationValue = element.annotationMirrors
            .asSequence()
            .filter { mirror ->
                val annotationType = processingEnvironment.elementUtils.getTypeElement(AnnotateClassWith::class.java.canonicalName.toString()).asType()
                processingEnvironment.typeUtils.isSameType(mirror.annotationType, annotationType)
            }
            .map { mirror ->
                val entry = processingEnvironment.elementUtils.getElementValuesWithDefaults(mirror)
                entry.values.asSequence()
                    .filterNotNull()
                    .toList()
            }
            .firstOrNull()
            ?.firstOrNull()

        val annotations = annotationValue?.value?.toString()
            ?.split(",")
            ?: listOf()

        return annotations.map { name ->
            val fullName = name.substringBeforeLast(".class")
            ClassName("", fullName)
        }
    }

    private fun Element.findAnnotatedElements(): List<AnnotatedElement> {
        val elements: MutableList<AnnotatedElement> = mutableListOf()

        this.enclosedElements.forEach { element ->
            AnnotationHelper.propertyAnnotations.forEach { annotation ->
                val foundAnnotation = element.getAnnotation(annotation)
                if (foundAnnotation != null) {
                    val name = formatName(element.simpleName.toString())
                    val annotatedElement = AnnotatedElement(
                        name,
                        foundAnnotation,
                        element
                    )
                    elements.add(annotatedElement)
                }
            }

            elements += element.findAnnotatedElements()
        }

        return elements
    }

    private fun formatName(name: String): String {
        val getString = "get"
        val annotationsString = "\$annotations"

        return when {
            name.startsWith(getString) -> {
                name.substringAfter(getString)
                    .decapitalize()
            }
            name.contains(annotationsString) -> {
                name.substringBefore(annotationsString)
            }
            else -> {
                name
            }
        }
    }

    private fun getTypeOfProperty(annotation: Annotation): TypeName {
        val typeString = when (annotation) {
            is BooleanProperty -> "Boolean"
            is FloatProperty -> "Float"
            is IntProperty -> "Int"
            is LongProperty -> "Long"
            is StringProperty -> "String"
            else -> throw IllegalStateException("Invalid property annotation")
        }

        return ClassName("", typeString)
    }

    private fun getPropertyInitializerString(defaultValue: Any): String {
        val formatChar = when (defaultValue) {
            !is String -> "%L"
            else -> "%S"
        }

        return "%L(getString(%L), $formatChar, %N)"
    }
}