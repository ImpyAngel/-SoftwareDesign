class TagException(message: String) : Exception(message)

fun tagError(message: String): Nothing = throw TagException(message)