import java.util.*

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
class CalcVisitor : TokenVisitor<Int> {
    override fun answer(): Int {
        if (acc.empty()) error("Empty input")
        return acc.peek()
    }

    private val acc = Stack<Int>()
    override fun visit(token: Brace) = error("Brackets in calculator")

    override fun visit(token: Number) {
        acc.add(token.value)
    }

    override fun visit(token: Operation) {
        try {
            val second = acc.pop()
            val first = acc.pop()
            acc.push(token(first, second))
        } catch (e: EmptyStackException) {
            error("Bad count of number")
        }

    }
}