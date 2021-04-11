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

    @Test
    fun `strikes all except last frame`() {
        val total: Int = bowlingScore("X X X X X X X X X --")

        assertEquals(240, total)
    }

    @Test
    fun `perfect game`() {
        val total: Int = bowlingScore("X X X X X X X X X XXX")

        assertEquals(300, total)
    }

    @Test
    fun `perfect game but 1 pin on last throw`() {
        val total: Int = bowlingScore("X X X X X X X X X XX1")

        assertEquals(291, total)
    }

    @Test
    fun `spares on the third throw in the last frame`() {
        val total: Int = bowlingScore("X X X X X X X X X X1/")

        assertEquals(281, total)
    }
}

private fun bowlingScore(input: String): Int {
    return input.split(" ")
        .map { frame ->
            val throw1 = frame.substring(0, 1).parseNonSpareThrow()
            val nextThrows = frame
                .map { it.toString() }
                .zipWithNext { previous, current ->
                    current.parseSpareableThrow(previous)
                }
            Frame.from(listOf(throw1) + nextThrows)
        }
        .windowed(size = 3, step = 1, partialWindows = true) { window ->
            val nextThrows = window.drop(1).flatMap { it.throws }
            window.first().score(nextThrows)
        }
        .sum()
}

private fun String.parseNonSpareThrow() = when (this) {
    "-" -> 0
    "X" -> 10
    else -> toInt()
}

private fun String.parseSpareableThrow(previousThrow: String) = when (this) {
    "/" -> 10 - previousThrow.parseNonSpareThrow()
    else -> parseNonSpareThrow()
}

sealed class Frame(val throws: List<Int>) {
    abstract fun score(next: List<Int>): Int

    private class Pins(throws: List<Int>) : Frame(throws) {
        override fun score(next: List<Int>) = throws.sum()
    }

    private class Spare(throws: List<Int>) : Frame(throws) {
        override fun score(next: List<Int>) = (throws + next.take(1)).sum()
    }

    private class Strike(throws: List<Int>) : Frame(throws) {
        override fun score(next: List<Int>) = (throws + next.take(2)).sum()
    }

    companion object {
        fun from(throws: List<Int>): Frame = when {
            throws.size == 1 -> Strike(throws)
            throws.sum() == 10 -> Spare(throws)
            else -> Pins(throws)
        }
    }
}