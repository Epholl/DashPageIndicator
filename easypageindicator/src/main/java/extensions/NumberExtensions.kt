package extensions

/**
 * Created by Tomáš Isteník on 05/12/2017.
 */

infix fun Number.isRightOf(other: Number): Boolean = this.toDouble() > (other.toDouble())

infix fun Number.isLeftOf(other: Number): Boolean = this.toDouble() < (other.toDouble())
