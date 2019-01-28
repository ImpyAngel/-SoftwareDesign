/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
class PrintVisitor : TokenVisitor<Unit> {
    override fun answer() = Unit

    private fun Token.print() {
        print("$symbol ")
    }

    override fun visit(token: Brace) = token.print()

    override fun visit(token: Number) = token.print()

    override fun visit(token: Operation) = token.print()
}