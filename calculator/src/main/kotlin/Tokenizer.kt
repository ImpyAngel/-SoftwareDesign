import java.lang.IllegalStateException

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
class Tokenizer(private val source: String) {
    private enum class State {
        START, NUMBER, ERROR, END
    }

    private var state = State.START
    private var i = 0
    private var acc = 0
    private var answer = mutableListOf<Token>()

    private fun flushAcc() {
        answer.add(Number(acc))
        acc = 0
    }

    private fun next() {
        when(state) {
            State.START -> {
                if (i >= source.length) {
                    state = State.END
                } else {
                    val char = source[i]
                    if (char.isDigit()) {
                        acc = char.toInt() - '0'.toInt()
                        state = State.NUMBER
                    } else {
                        if (char != ' ') {
                            try {
                                answer.add(char.toToken())
                            } catch (e: IllegalStateException) {
                                println(e)
                                state = State.ERROR
                            }
                        }
                    }
                }
            }
            State.NUMBER -> {
                if (i >= source.length) {
                    state = State.END
                    flushAcc()
                } else {
                    val char = source[i]
                    if (char.isDigit()) {
                        acc = acc * 10 + char.toInt() - '0'.toInt()
                    } else {
                        flushAcc()
                        state = State.START
                        return next()
                    }
                }
            }
            State.ERROR -> {}
            State.END -> {}
        }
        i++
    }

    fun tokenize(): List<Token>? {
        while (state != State.END && state != State.ERROR) next()

        return answer.takeIf { state == State.END }
    }

    private fun Char.toToken(): Token = when(this) {
        '(' -> Brace.Left
        ')' -> Brace.Right
        '+' -> Operation.Plus
        '-' -> Operation.Minus
        '*' -> Operation.Times
        '/' -> Operation.Div
        else -> error("unsupported symbol \"$this\"")
    }
}