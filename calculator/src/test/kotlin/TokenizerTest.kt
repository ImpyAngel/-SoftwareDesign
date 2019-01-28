import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
class Test {
    @Test
    fun tokenize() {
        val tokenized = Tokenizer("(3 + 3* 5 - 12 / 3 + ((2) * 3  ))  -3").tokenize()!!
        assertEquals("(3+3*5-12/3+((2)*3))-3", tokenized.joinToString(separator = "") { it.symbol})
    }

    @Test
    fun parserTest() {
        val tokenized = Tokenizer("(3 + 3* 5 - 12 / 3 + ((2) * 3  ))  -3").tokenize()!!
        val parsed = ParserVisitor().applyVisitor(tokenized)
        assertEquals("3 3 5 * + 12 3 / - 2 3 * + 3 -", parsed.joinToString(separator = " ") { it.symbol })
    }

    @Test
    fun parserTest2() {
        val tokenized = Tokenizer("3 + 4 * 2 / (1 - 5)").tokenize()!!
        val parsed = ParserVisitor().applyVisitor(tokenized)
        assertEquals("3 4 2 * 1 5 - / +", parsed.joinToString(separator = " ") { it.symbol })
    }


    @Test
    fun calcTest() {
        val tokenized = Tokenizer("(3 + 3* 5 - 12 / 3 + ((2) * 3  ))  -3").tokenize()!!
        val ans = CalcVisitor().applyVisitor(ParserVisitor().applyVisitor(tokenized))
        assertEquals(17, ans)
    }

    @Test
    fun calcTest2() {
        val tokenized = Tokenizer("(3) + 2  / 1 + 32 * 12 -2 * 10 - 23 + 1").tokenize()!!
        val ans = CalcVisitor().applyVisitor(ParserVisitor().applyVisitor(tokenized))
        assertEquals(347, ans)
    }
}