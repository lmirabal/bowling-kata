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

    @Test
    fun `knocks down an extra pin with each throw`() {
        val total: Int = bowlingScore("-- 1- 2- 3- 4- 5- 6- 7- 8- 9-")

        assertEquals(45, total)
    }
}

private fun bowlingScore(input: String): Int {
    val frames = input.split(" ")
        .map { frame ->
            val throw1 = frame.substring(0, 1).parseThrow()
            val throw2 = frame.substring(1, 2).parseThrow()
            throw1 + throw2
        }
    return frames.sum()
}

private fun String.parseThrow() = this.toIntOrNull() ?: 0