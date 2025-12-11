package lab.`is`.bank.artifact.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.client.http.javanet.NetHttpTransport
import lab.`is`.bank.artifact.dto.UpdateArtifactRequest
import lab.`is`.bank.artifact.service.impl.AiOperatorServiceImpl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AiOperatorServiceIntegrationTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var aiService: AiOperatorServiceImpl
    private val objectMapper = ObjectMapper()

    @BeforeAll
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8000)
        aiService = AiOperatorServiceImpl(NetHttpTransport(), objectMapper)
    }

    @AfterAll
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @AfterEach
    fun resetServer() {
        while (mockWebServer.requestCount > 0) {
            mockWebServer.takeRequest()
        }
    }

    @Test
    @DisplayName("validateArtifact should return true when AI service responds with true")
    fun testValidateArtifactSuccess() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody("true")
                .setResponseCode(200)
                .addHeader("Content-Type", "text/plain")
        )

        val result = aiService.validateArtifact("Elder_Wand", "wizard123")

        assertTrue(result)
        val request = mockWebServer.takeRequest()
        assertEquals("/validate-save", request.path)
        assertEquals("POST", request.method)
    }

    @Test
    @DisplayName("validateArtifact should return false when AI service responds with false")
    fun testValidateArtifactFailure() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody("false")
                .setResponseCode(200)
                .addHeader("Content-Type", "text/plain")
        )

        val result = aiService.validateArtifact("Cursed_Item", "dark_wizard")

        assertFalse(result)
    }

    @Test
    @DisplayName("levelOfDanger should return danger level string")
    fun testLevelOfDanger() {
        val expectedLevel = "HIGH"
        mockWebServer.enqueue(
            MockResponse()
                .setBody(expectedLevel)
                .setResponseCode(200)
                .addHeader("Content-Type", "text/plain")
        )

        val result = aiService.levelOfDanger("Deathly_Hallows")

        assertEquals(expectedLevel, result)
        val request = mockWebServer.takeRequest()
        assertEquals("/level-of-danger", request.path)
    }

    @Test
    @DisplayName("getSpecification should return artifact specification")
    fun testGetSpecification() {
        val expectedSpec = "A powerful magical artifact"
        mockWebServer.enqueue(
            MockResponse()
                .setBody(expectedSpec)
                .setResponseCode(200)
                .addHeader("Content-Type", "text/plain")
        )

        val result = aiService.getSpecification("Time_Turner")

        assertEquals(expectedSpec, result)
        val request = mockWebServer.takeRequest()
        assertEquals("/get-specification", request.path)
    }

    @Test
    @DisplayName("getAllArtifact should parse and return list of artifacts")
    fun testGetAllArtifacts() {
        val mockResponse = """
            [
                {"Name": "Elder_Wand", "Lvl": "HIGH"},
                {"Name": "Invisibility_Cloak", "Lvl": "MEDIUM"}
            ]
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setBody(mockResponse)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
        )

        val result = aiService.getAllArtifact()

        assertEquals(2, result.size)
        assertEquals("Elder_Wand", result[0].name)
        assertEquals("HIGH", result[0].magicalProperty?.dangerLevel)
        assertEquals("Invisibility_Cloak", result[1].name)
        assertEquals("MEDIUM", result[1].magicalProperty?.dangerLevel)
    }

    @Test
    @DisplayName("validateDescription should return true for valid description")
    fun testValidateDescription() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody("true")
                .setResponseCode(200)
                .addHeader("Content-Type", "text/plain")
        )

        val result = aiService.validateDescription("Storing for research purposes")

        assertTrue(result)
        val request = mockWebServer.takeRequest()
        assertEquals("/description-validation", request.path)
    }

    @Test
    @DisplayName("addBannedUser should return true on successful ban")
    fun testAddBannedUser() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
        )

        val result = aiService.addBannedUser("evil_wizard")

        assertTrue(result)
        val request = mockWebServer.takeRequest()
        assertEquals("/add-ban-user", request.path)
    }

    @Test
    @DisplayName("addBannedUser should return false on failure")
    fun testAddBannedUserFailure() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
        )

        val result = aiService.addBannedUser("user123")

        assertFalse(result)
    }

    @Test
    @DisplayName("addBannedWord should return true on successful ban")
    fun testAddBannedWord() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
        )

        val result = aiService.addBannedWord("avada_kedavra")

        assertTrue(result)
        val request = mockWebServer.takeRequest()
        assertEquals("/add-ban-word", request.path)
    }

    @Test
    @DisplayName("requestUpdate should send update request successfully")
    fun testRequestUpdate() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
        )

        val updateRequest = UpdateArtifactRequest(
            name = "Elder_Wand",
            newDangerLevel = "EXTREME"
        )

        assertDoesNotThrow {
            aiService.requestUpdate(updateRequest)
        }

        val request = mockWebServer.takeRequest()
        assertEquals("/request-update-person", request.path)
        assertEquals("POST", request.method)
    }

    @Test
    @DisplayName("AI service should handle network errors gracefully")
    fun testNetworkErrorHandling() {
        mockWebServer.shutdown()

        assertThrows<Exception> {
            aiService.validateArtifact("test", "test")
        }

        mockWebServer = MockWebServer()
        mockWebServer.start(8000)
        aiService = AiOperatorServiceImpl(NetHttpTransport(), objectMapper)
    }
}
