package pl.kamilszustak.shark

import pl.kamilszustak.shark.annotations.CustomTypeProvider

object ListProvider {

    @CustomTypeProvider
    @JvmStatic
    fun getList(): List<String> = listOf()
}