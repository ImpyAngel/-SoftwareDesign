private fun printLog(log: Any) = println(log)


object Logger {
    private var unableDebug = false

    fun info(log: Any): LoggerBuilder {
        printLog("[INFO] - $log")
        return LoggerBuilder()
    }
    fun error(log: Any): LoggerBuilder {
        printLog("[ERROR] - $log")
        return LoggerBuilder()
    }
    fun debug(log: () -> Any): LoggerBuilder {
        if (unableDebug) {
            printLog("[DEBUG] - ${ log() }")
        }
        return LoggerBuilder()

    }
}
class LoggerBuilder {
    fun add(log: Any): LoggerBuilder {
        printLog("\t\t$log")
        return LoggerBuilder()
    }
}