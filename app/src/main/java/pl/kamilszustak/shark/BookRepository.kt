package pl.kamilszustak.shark

import pl.kamilszustak.shark.annotations.BooleanProperty
import pl.kamilszustak.shark.annotations.IntProperty
import pl.kamilszustak.shark.annotations.Repository
import pl.kamilszustak.shark.annotations.StringProperty
import pl.kamilszustak.shark.core.Property

@Repository(nameResource = R.string.book_repository)
interface BookRepository {

    @IntProperty(keyResource = R.string.page_number)
    val pageNumber: Property<Int>

    @StringProperty(keyResource = R.string.book_title)
    val bookTitle: Property<String>

    @BooleanProperty(keyResource = R.string.is_started)
    val isStarted: Property<Boolean>
}