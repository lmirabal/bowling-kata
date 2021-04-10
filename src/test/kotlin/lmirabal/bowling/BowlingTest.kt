package lmirabal.bowling

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BowlingTest {
    @Test
    fun `all misses`() {
        val total: Int = bowlingScore("-- -- -- -- -- -- -- -- -- --")

        assertEquals(0, total)
    }
}

private fun bowlingScore(input: String): Int {
    return 0
}