/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */

interface TokenVisitor<T> {
    fun visit(token: Brace)
    fun visit(token: Number)
    fun visit(token: Operation)

    fun answer(): T
    fun applyVisitor(tokens: List<Token>): T {
        tokens.forEach {
            it.accept(this)
        }
        return answer()
    }
}


