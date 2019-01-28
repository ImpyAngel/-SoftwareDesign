import java.util.*

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
class ParserVisitor : TokenVisitor<List<Token>> {
    override fun answer(): List<Token> {
        return answer + acc.toList().reversed()
    }

    private val answer = mutableListOf<Token>()
    private val acc = Stack<Token>()

    override fun visit(token: Brace) {
        when (token) {
            Brace.Left -> acc.push(token)
            Brace.Right -> {
                while (!acc.empty() && acc.peek() != Brace.Left) {
                    answer.add(acc.pop())
                }
                if (acc.empty()) {
                    error("Bad brackets")
                } else acc.pop()
            }
        }
    }

    override fun visit(token: Number) {
        answer.add(token)
    }

    override fun visit(token: Operation) {
        while (!acc.empty() && acc.peek().let { it is Operation && it.priority >= token.priority }) {
            answer.add(acc.pop())
        }
        acc.add(token)
    }
}
