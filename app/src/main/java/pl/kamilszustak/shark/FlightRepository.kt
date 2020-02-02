package pl.kamilszustak.shark

import pl.kamilszustak.shark.annotations.BooleanProperty
import pl.kamilszustak.shark.annotations.IntProperty
import pl.kamilszustak.shark.annotations.Repository
import pl.kamilszustak.shark.annotations.StringProperty

@Repository(nameResource = R.string.flight_repository, isEncrypted = true)
abstract class FlightRepository {

    @IntProperty(keyResource = R.string.seat_number)
    abstract val seatNumber: Int

    @StringProperty(keyResource = R.string.flight_code, defaultValue = "ABC123")
    abstract fun getFlightCode(): String

    @BooleanProperty(keyResource = R.string.is_abroad, defaultValue = true)
    abstract val isAbroad: Boolean
}