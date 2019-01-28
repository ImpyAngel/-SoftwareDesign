import java.time.Instant

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
interface Clock {
    val now: Instant
}

class NormalClock : Clock {
    override val now: Instant
        get() = Instant.now()
}

class SettableClock(override var now: Instant) : Clock