package pl.kamilszustak.shark

import pl.kamilszustak.shark.annotations.BooleanProperty
import pl.kamilszustak.shark.annotations.IntProperty
import pl.kamilszustak.shark.annotations.Repository
import pl.kamilszustak.shark.annotations.StringProperty
import pl.kamilszustak.shark.core.Property
import pl.kamilszustak.shark.core.PropertyRepository

@Repository(nameResource = R.string.flight_repository, isEncrypted = true)
interface FlightRepository : PropertyRepository {

    @IntProperty(keyResource = R.string.seat_number)
    val seatNumber: Property<Int>

    @StringProperty(keyResource = R.string.flight_code, defaultValue = "ABC123")
    val flightCode: Property<String>

    @BooleanProperty(keyResource = R.string.is_abroad, defaultValue = true)
    val isAbroad: Property<Boolean>
}