package pl.kamilszustak.shark.compiler

import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

class ProviderStore {
    private val providers: MutableMap<TypeMirror, ExecutableElement> = hashMapOf()

    fun add(provider: Pair<TypeMirror, ExecutableElement>) {
        providers[provider.first] = provider.second
    }

    fun getProviderFor(type: TypeMirror): ExecutableElement? =
        providers[type]
}