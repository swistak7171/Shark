package pl.kamilszustak.shark

import pl.kamilszustak.shark.annotations.DefaultValueProvider

object ListProvider {

    @DefaultValueProvider
    @JvmStatic
    fun getList(): List<String> = listOf()
}