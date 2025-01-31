package lab.`is`.bank.services.artifactManagement.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpResponse
import com.google.api.client.http.javanet.NetHttpTransport
import lab.`is`.bank.dto.artifact.ArtifactDto
import lab.`is`.bank.dto.artifact.MagicalPropertyDto
import lab.`is`.bank.services.artifactManagement.interfaces.ArtifactValidationService
import org.springframework.stereotype.Service
import java.io.InputStreamReader

@Service
class ArtifactValidationService(
    private val httpTransport: NetHttpTransport = NetHttpTransport(),
    private val objectMapper: ObjectMapper = ObjectMapper()
) : ArtifactValidationService {

    private val AI_SERVER = "http://localhost:8000"
    private val IS_SAVABLE = "$AI_SERVER/validate-save"
    private val DANGER_LVL = "$AI_SERVER/level-of-danger"
    private val GET_ALL = "$AI_SERVER/get-all"
    private val GET_ALL_LVL = "$AI_SERVER/get-all/lvl"

    override fun validateArtifact(artifactName: String): Boolean {
        val url = GenericUrl(IS_SAVABLE)
        val content = mapOf("user_input" to artifactName)

        val req: HttpRequest = httpTransport.createRequestFactory()
            .buildPostRequest(url, com.google.api.client.http.json.JsonHttpContent(
                com.google.api.client.json.gson.GsonFactory.getDefaultInstance(), content))

        val response: HttpResponse = req.execute()
        val responseText = response.parseAsString()
        response.disconnect()
        return responseText.toBoolean()
    }

    override fun levelOfDanger(artifactName: String): String {
        val url = GenericUrl(DANGER_LVL)
        val content = mapOf("user_input" to artifactName)

        val req: HttpRequest = httpTransport.createRequestFactory()
            .buildPostRequest(url, com.google.api.client.http.json.JsonHttpContent(
                com.google.api.client.json.gson.GsonFactory.getDefaultInstance(), content))

        val response: HttpResponse = req.execute()
        val responseText = response.parseAsString()
        response.disconnect()
        return responseText
    }

    override fun getAllArtifact(): List<ArtifactDto> {
        val url = GenericUrl(GET_ALL)

        val req: HttpRequest = httpTransport.createRequestFactory()
            .buildGetRequest(url)

        val response: HttpResponse = req.execute()
        val responseBody = InputStreamReader(response.content).use { it.readText() }
        response.disconnect()

        val list: List<ArtifactDataFromAiServer> = objectMapper.readValue(
            responseBody,
            objectMapper.typeFactory.constructCollectionType(
                List::class.java,
                ArtifactDataFromAiServer::class.java
            )
        )
        val artifacts = mutableListOf<ArtifactDto>()
        for(aiArtifact in list) {

            val artifact = ArtifactDto(
                name = aiArtifact.Name,
                magicalProperty = MagicalPropertyDto(dangerLevel = aiArtifact.Lvl)
            )

            artifacts.add(artifact)
        }
        return artifacts;
    }

    override fun getAllArtifact(dangerousLevel: String): List<ArtifactDto> {
        val url = GenericUrl("$GET_ALL_LVL/$dangerousLevel")

        val req: HttpRequest = httpTransport.createRequestFactory()
            .buildGetRequest(url)

        val response: HttpResponse = req.execute()
        val responseBody = InputStreamReader(response.content).use { it.readText() }
        response.disconnect()

        return objectMapper.readValue(responseBody, objectMapper.typeFactory.constructCollectionType(List::class.java, ArtifactDto::class.java))
    }

    /**
     * Локальный класс для удобного маппинга данных с сервера
     */
    private data class ArtifactDataFromAiServer(
        val Name: String,
        val Lvl: String
    )
}