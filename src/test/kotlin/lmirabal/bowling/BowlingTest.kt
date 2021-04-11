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

    @Test
    fun `knocks down 5 pins and spares in each frame with a final 7`() {
        val total = bowlingScore("5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/ 5/7")

        assertEquals(152, total)
    }
}

private fun bowlingScore(input: String): Int {
    return input.split(" ")
        .map { frame ->
            val throw1 = frame.substring(0, 1).parse9PinThrow()
            val nextThrows = frame.drop(1).map { it.toString().parseNextThrow(throw1) }
            Frame.from(listOf(throw1) + nextThrows)
        }
        .windowed(size = 2, step = 1, partialWindows = true) { window ->
            val nextFrame = window.last()
            window.first().score(nextFrame.firstThrow)
        }
        .sum()
}

private fun String.parse9PinThrow() = when (this) {
    "-" -> 0
    else -> toInt()
}

private fun String.parseNextThrow(throw1: Int) = when (this) {
    "/" -> 10 - throw1
    else -> parse9PinThrow()
}

sealed class Frame(val throws: List<Int>) {
    val firstThrow = throws.first()
    abstract fun score(next: Int): Int

    private class Pins(throws: List<Int>) : Frame(throws) {
        override fun score(next: Int) = throws.sum()
    }

    private class Spare(throws: List<Int>) : Frame(throws) {
        override fun score(next: Int) = throws.sum() + next
    }

    companion object {
        fun from(throws: List<Int>): Frame =
            if (throws.sum() == 10) Spare(throws)
            else Pins(throws)
    }
}