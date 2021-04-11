package lmirabal.bowling

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BowlingTest {
    @Test
    fun `all misses`() {
        val total = bowlingScore("-- -- -- -- -- -- -- -- -- --")

        assertEquals(0, total)
    }

    @Test
    fun `knocks down 1 pin in each frame`() {
        val total = bowlingScore("1- 1- 1- 1- 1- 1- 1- 1- 1- 1-")

        assertEquals(10, total)
    }

    @Test
    fun `knocks down 1 pin in each throw`() {
        val total = bowlingScore("11 11 11 11 11 11 11 11 11 11")

        assertEquals(20, total)
    }

    @Test
    fun `knocks down an extra pin with each throw`() {
        val total = bowlingScore("-- 1- 2- 3- 4- 5- 6- 7- 8- 9-")

        assertEquals(45, total)
    }

    @Test
    fun `spares in first frame and knocks down 5 in the others`() {
        val total = bowlingScore("-/ 5- 5- 5- 5- 5- 5- 5- 5- 5-")

        assertEquals(60, total)
    }

    @Test
    fun `knocks down 5 pins and spares except last frame`() {
        val total = bowlingScore("5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ --")

        assertEquals(130, total)
    }
}

private fun bowlingScore(input: String): Int {
    val frames = input.split(" ")
        .map { frame ->
            val throw1 = frame.substring(0, 1).parse9PinThrow()
            val throw2 = frame.substring(1, 2).parseSecondThrow(throw1)
            Frame(throw1, throw2)
        }
    var total = 0
    for (i in frames.indices) {
        val current = frames[i].score()
        total += if (current < 10) current else current + frames[i + 1].throw1
    }
    return total
}

private fun String.parse9PinThrow() = when (this) {
    "-" -> 0
    else -> toInt()
}

private fun String.parseSecondThrow(throw1: Int) = when (this) {
    "/" -> 10 - throw1
    else -> parse9PinThrow()
}

data class Frame(val throw1: Int, val throw2: Int) {
    fun score() = throw1 + throw2
}