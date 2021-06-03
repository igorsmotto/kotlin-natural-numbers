import Natural.Successor
import Natural.Zero

/**
 * Kotlin Natural numbers
 * A playground for implementing the natural numbers in number theory
 * A natural number can be either zero or a successor of zero
 * i.e. 5 = Succ(Succ(Succ(Succ(Succ(Zero)))))
 *
 * Caveats:
 * - "5 - 6" can result in either 0 or an error, I opted for the former
 * - "3 / 2" should return the natural division (1)
 * - Division by 0 can result in either infinity or error, I opted for the latter
 *
 */

sealed class Natural {
    object Zero : Natural()
    class Successor(val predecessor: Natural): Natural()

    override operator fun equals(other: Any?): Boolean {
        val castedOther = other as? Natural ?: return false

        if (this is Zero && castedOther is Zero) return true

        if (this is Successor && castedOther is Successor) {
            return this.predecessor == castedOther.predecessor
        }

        return false
    }
}

operator fun Natural.inc(): Natural {
    return Successor(this)
}

operator fun Natural.dec(): Natural {
    return when (this) {
        is Zero -> Zero
        is Successor -> this.predecessor
    }
}

operator fun Natural.plus(b: Natural): Natural {
    return when (this) {
        is Zero -> b
        is Successor -> {
            when (b) {
                is Zero -> this
                is Successor -> this.predecessor + Successor(b)
            }
        }
    }
}

operator fun Natural.minus(b: Natural): Natural {
    return when (b) {
        is Zero -> this
        is Successor -> {
            when (this) {
                is Zero -> Zero
                is Successor -> this.predecessor - b.predecessor
            }
        }
    }
}

operator fun Natural.times(b: Natural): Natural {
    return when (this) {
        is Zero -> Zero
        is Successor -> {
            when (b) {
                is Zero -> Zero
                is Successor -> this.predecessor * b.predecessor
            }
        }
    }
}

operator fun Natural.compareTo(b: Natural): Int {
    if (this is Zero && b is Zero) return 0

    return when (this) {
        is Zero -> -1
        is Successor -> {
            when (b) {
                is Zero -> 1
                is Successor -> this.predecessor.compareTo(b.predecessor)
            }
        }
    }
}

operator fun Natural.div(b: Natural): Natural {
    return when (this) {
        is Zero -> Zero
        is Successor -> {
            when (b) {
                is Zero -> throw ArithmeticException("Division by 0 is not allowed")
                is Successor -> {
                    return if (this < b) {
                        Zero
                    } else {
                        Successor(Zero) + (this - b) / b
                    }
                }
            }
        }
    }
}

val one: Natural = Successor(Zero)
val two: Natural = Successor(one)
val four: Natural = Successor(Successor(two))

fun main () {
    // Adition
    assert(one + one == two)

    // Incr
    var x: Natural = Successor(Zero)
    x++
    assert(x == two)

    // Subtraction
    assert(two - two == Zero)
    assert(two - one == one)
    assert(one - two == Zero)

    // Multiplication
    assert(one * one == one)
    assert(two * two == four)
    assert(four * Zero == Zero)

    // Compare
    assert(one < two)
    assert(two > one)
    assert(two >= two)

    // Division
    assert(four / two == two)
    assert(four / one == four)
    assert(Successor(two) / two == one)
}