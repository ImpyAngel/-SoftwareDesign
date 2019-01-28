import java.util.*

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
fun main(args: Array<String>) {
    Scanner(System.`in`).use {
        val source = it.nextLine()
        val tokenized = Tokenizer(source).tokenize()
        if (tokenized != null) {
            val parsed = ParserVisitor().applyVisitor(tokenized)
            PrintVisitor().applyVisitor(parsed)
            println()
            val answer = CalcVisitor().applyVisitor(parsed)
            println("Answer is $answer")
        } else {
            println("Error in tokenize")
        }

    }
}
