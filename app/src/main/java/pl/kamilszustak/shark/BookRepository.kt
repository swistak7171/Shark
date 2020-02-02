package pl.kamilszustak.shark

import pl.kamilszustak.shark.annotations.BooleanProperty
import pl.kamilszustak.shark.annotations.IntProperty
import pl.kamilszustak.shark.annotations.Repository
import pl.kamilszustak.shark.annotations.StringProperty

@Repository(nameResource = R.string.book_repository)
interface BookRepository {

    @IntProperty(keyResource = R.string.page_number)
    val pageNumber: Int

    @StringProperty(keyResource = R.string.book_title)
    val bookTitle: String

    @BooleanProperty(keyResource = R.string.is_started)
    val isStarted: Boolean
}