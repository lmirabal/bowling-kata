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

    @Test
    fun `spares in first frame and knocks down 5 in the others`() {
        val total: Int = bowlingScore("-/ 5- 5- 5- 5- 5- 5- 5- 5- 5-")

        assertEquals(60, total)
    }
}

private fun bowlingScore(input: String): Int {
    val throws = input.split(" ")
        .flatMap { frame ->
            val throw1 = frame.substring(0, 1).parseThrow()
            val throw2 = frame.substring(1, 2).parseThrow()
            listOf(throw1, throw2)
        }
    var total = 0
    for (i in throws.indices) {
        val current = throws[i]
        total += if (current != 10) current else current + throws[i + 1]
    }
    return total
}

private fun String.parseThrow() = when (this) {
    "/" -> 10
    "-" -> 0
    else -> toInt()
}