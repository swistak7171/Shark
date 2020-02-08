package pl.kamilszustak.shark

import pl.kamilszustak.shark.annotations.CustomTypeProvider

object ListProvider {

    @CustomTypeProvider
    fun getList(): List<String> = listOf()
}