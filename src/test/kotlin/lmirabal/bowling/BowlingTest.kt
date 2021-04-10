package lmirabal.bowling

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BowlingTest {
    @Test
    fun `all misses`() {
        val total: Int = bowlingScore("-- -- -- -- -- -- -- -- -- --")

        assertEquals(0, total)
    }

    @Test
    fun `knocks down 1 pin in each frame`() {
        val total: Int = bowlingScore("1- 1- 1- 1- 1- 1- 1- 1- 1- 1-")

        assertEquals(10, total)
    }

    @Test
    fun `knocks down 1 pin in each throw`() {
        val total: Int = bowlingScore("11 11 11 11 11 11 11 11 11 11")

        assertEquals(20, total)
    }
}

private fun bowlingScore(input: String): Int {
    val frames = input.split(" ")
        .map { frame ->
            val throw1 = if (frame.substring(0, 1) == "-") 0 else 1
            val throw2 = if (frame.substring(1, 2) == "-") 0 else 1
            throw1 + throw2
        }
    return frames.sum()
}