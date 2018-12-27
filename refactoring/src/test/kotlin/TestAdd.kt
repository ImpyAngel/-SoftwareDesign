import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ru.akirakozov.sd.refactoring.DBLayer
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet
import ru.akirakozov.sd.refactoring.servlet.QueryServlet
import java.io.*
import java.sql.DriverManager
import java.sql.Statement
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.test.assertEquals

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */

class TestAdd {
    companion object {
        private val dbLayer = DBLayer()

        @BeforeAll
        @JvmStatic
        fun beforeAll() = with(dbLayer){
            tryCreate()
            cleanUp()
        }
    }

    @AfterEach
    fun afterEach() {
        dbLayer.cleanUp()
    }

    private fun mockAddRequest(i: Int) = mock<HttpServletRequest> {
        on { getParameter("name") } doAnswer { "product$i" }
        on { getParameter("price") } doAnswer { "${i}00" }
        on { getMethod() } doReturn "GET"
    }

    private val mockRequest = mock<HttpServletRequest> {
        on { getMethod() } doReturn "GET"
    }

    private val mockResponse = mock<HttpServletResponse> {
        on { getWriter() } doReturn PrintWriter(StringWriter())
    }

    private fun mockQuery(query: String) = mock<HttpServletRequest> {
        on { getParameter("command") } doReturn query
        on { getMethod() } doReturn "GET"
    }

    @Test
    fun baseAdd() {
        val stringWriter = StringWriter()
        val writer = PrintWriter(stringWriter)
        val mockResponse = mock<HttpServletResponse> {
            on { getWriter() } doReturn writer
        }

        AddProductServlet().service(mockAddRequest(1), mockResponse)

        writer.flush()
        assertEquals("OK\n", stringWriter.toString())
    }

    @Test
    fun baseGet() {
        val stringWriter = StringWriter()
        val writer = PrintWriter(stringWriter)

        val mockGetResponse = mock<HttpServletResponse> {
            on { getWriter() } doReturn writer
        }

        AddProductServlet().service(mockAddRequest(1), mockResponse)
        AddProductServlet().service(mockAddRequest(2), mockResponse)
        GetProductsServlet().service(mockRequest, mockGetResponse)

        writer.flush()
        val goodHtml = String(FileInputStream("src/test/resources/goldBaseGet.html").readAllBytes())

        assertEquals(goodHtml, stringWriter.toString())
    }

    private fun queryTest(query: String, goldName: String) {
        val stringWriter = StringWriter()
        val writer = PrintWriter(stringWriter)

        val mockGetResponse = mock<HttpServletResponse> {
            on { getWriter() } doReturn writer
        }

        AddProductServlet().service(mockAddRequest(5), mockResponse)
        AddProductServlet().service(mockAddRequest(1), mockResponse)
        AddProductServlet().service(mockAddRequest(3), mockResponse)
        AddProductServlet().service(mockAddRequest(2), mockResponse)
        AddProductServlet().service(mockAddRequest(7), mockResponse)
        AddProductServlet().service(mockAddRequest(6), mockResponse)

        QueryServlet().service(mockQuery(query), mockGetResponse)

        writer.flush()

        val goldHtml = String(FileInputStream("src/test/resources/$goldName.html").readAllBytes())

        assertEquals(goldHtml, stringWriter.toString())
    }

    @Test
    fun minGet() {
        queryTest("min", "goldMinGet")
    }

    @Test
    fun maxGet() {
        queryTest("max", "goldMaxGet")
    }

    @Test
    fun countGet() {
        queryTest("count", "goldCountGet")
    }

    @Test
    fun sumGet() {
        queryTest("sum", "goldSumGet")
    }

    @Test
    fun wrongGet() {
        queryTest("afafa", "goldWrongGet")
    }
}
