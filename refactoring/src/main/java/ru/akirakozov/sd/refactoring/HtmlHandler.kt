package ru.akirakozov.sd.refactoring

import java.io.PrintWriter
import javax.servlet.http.HttpServletResponse

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */

class HtmlHandler private constructor(private val writer: PrintWriter) {

    fun printLine(line: Any) = writer.println(line)

    fun addHeader(header: String) = printLine("<h1>$header</h1>")

    fun addDbEntity(product: Product) = printLine(product.name + "\t" + product.price + "</br>")

    fun addDbEntity(products: List<Product>) = products.forEach { addDbEntity(it) }

    companion object {
        fun createHtml(response: HttpServletResponse, plainText: Boolean = false, block: HtmlHandler.() -> Unit) {
            val writer = response.writer
            if (!plainText) writer.println("<html><body>")
            block(HtmlHandler(writer))
            if (!plainText) writer.println("</body></html>")


            response.contentType = if (!plainText) "text/html" else "text/plain"
            response.status = HttpServletResponse.SC_OK
        }
    }
}

class HttpCall(private val dbLayer: DBLayer, private val response: HttpServletResponse) {
    fun add(name: String, price: Int) = HtmlHandler.createHtml(response, plainText = true)  {
        dbLayer.add(name, price)
        printLine("OK")
    }

    fun getAll() = HtmlHandler.createHtml(response) {
        addDbEntity(dbLayer.all())
    }

    fun getMin() = HtmlHandler.createHtml(response) {
        addHeader("Product with min price: ")
        addDbEntity(dbLayer.min())
    }

    fun getMax() = HtmlHandler.createHtml(response) {
        addHeader("Product with max price: ")
        addDbEntity(dbLayer.max())
    }

    fun getCount() = HtmlHandler.createHtml(response) {
        printLine("Number of products: ")
        printLine(dbLayer.count())
    }

    fun getSum() = HtmlHandler.createHtml(response) {
        printLine("Summary price: ")
        printLine(dbLayer.sum())
    }

    fun unknown(name: String) = HtmlHandler.createHtml(response, plainText = true) {
        printLine("Unknown command: $name")
    }
}