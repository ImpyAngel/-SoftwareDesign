/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */

interface Token {
    fun accept(visitor: TokenVisitor<*>)
    val symbol: String
}
class Number(val value: Int) : Token {
    override val symbol: String
        get() = value.toString()

    override fun accept(visitor: TokenVisitor<*>) {
        visitor.visit(this)
    }
}


sealed class Brace(override val symbol: String): Token {
    override fun accept(visitor: TokenVisitor<*>) {
        visitor.visit(this)
    }
    object Left : Brace("(")

    object Right : Brace(")")
}

sealed class Operation(val priority: Int, override val symbol: String): Token {
    abstract operator fun invoke(u: Int, v: Int): Int

    override fun accept(visitor: TokenVisitor<*>) {
        visitor.visit(this)
    }

    object Plus : Operation(1, "+") {
        override fun invoke(u: Int, v: Int): Int = u + v
    }

    object Minus :  Operation(1, "-") {
        override fun invoke(u: Int, v: Int): Int = u - v
    }

    object Times : Operation(2, "*") {
        override fun invoke(u: Int, v: Int): Int = u * v
    }

    object Div : Operation(2, "/") {
        override fun invoke(u: Int, v: Int): Int = u / v
    }
}