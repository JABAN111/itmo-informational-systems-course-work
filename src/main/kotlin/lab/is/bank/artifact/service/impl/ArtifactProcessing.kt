package lab.`is`.bank.artifact.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.http.json.JsonHttpContent
import com.google.api.client.json.gson.GsonFactory
import lab.`is`.bank.artifact.dto.ArtifactDto
import lab.`is`.bank.artifact.dto.MagicalPropertyDto
import lab.`is`.bank.artifact.dto.UpdateArtifactRequest
import lab.`is`.bank.artifact.service.interfaces.AiOperatorService
import org.springframework.stereotype.Service
import java.io.InputStreamReader

@Service
class AiOperatorServiceImpl(
    private val httpTransport: NetHttpTransport = NetHttpTransport(),
    private val objectMapper: ObjectMapper = ObjectMapper()
) : AiOperatorService {

    private val AI_SERVER = "http://localhost:8000"
    private val IS_SAVABLE = "$AI_SERVER/validate-save"
    private val DANGER_LVL = "$AI_SERVER/level-of-danger"
    private val GET_ALL = "$AI_SERVER/get-all"
    private val GET_ALL_LVL = "$AI_SERVER/get-all"
    private val DESCRIPTION_VALIDATION = "$AI_SERVER/description-validation"
    private val BAN_USER = "$AI_SERVER/add-ban-user"
    private val BAN_WORD = "$AI_SERVER/add-ban-word"
    private val GET_SPECIFIACTION = "$AI_SERVER/get-specification"
    private val UPDATE_REQUEST = "$AI_SERVER/request-update-person"

    override fun getSpecification(artifactName: String): String {
        val url = GenericUrl(GET_SPECIFIACTION)
        val content = mapOf(
            "user_input" to artifactName
        )

        val req: HttpRequest = httpTransport.createRequestFactory()
            .buildPostRequest(url, JsonHttpContent(GsonFactory.getDefaultInstance(), content))

        val response: HttpResponse = req.execute()
        val responseText = response.parseAsString()
        response.disconnect()

        return responseText
    }

    override fun requestUpdate(updateArtifact: UpdateArtifactRequest) {
        val url = GenericUrl(UPDATE_REQUEST)
        val content = mapOf(
            "user_input" to updateArtifact.name,
            "new_value" to updateArtifact.newDangerLevel,
        )

        val req: HttpRequest = httpTransport.createRequestFactory().buildPostRequest(
            url, JsonHttpContent(GsonFactory.getDefaultInstance(), content)
        )
        val response: HttpResponse = req.execute()
        response.disconnect()
    }

    override fun validateArtifact(artifactName: String, userAccountName: String): Boolean {
        val url = GenericUrl(IS_SAVABLE)
        val content = mapOf(
            "user_input" to artifactName,
            "user_account_name" to userAccountName
        )

        val req: HttpRequest = httpTransport.createRequestFactory()
            .buildPostRequest(url, JsonHttpContent(GsonFactory.getDefaultInstance(), content))

        val response: HttpResponse = req.execute()
        val responseText = response.parseAsString()
        response.disconnect()
        return responseText.toBoolean()
    }

    override fun levelOfDanger(artifactName: String): String {
        val url = GenericUrl(DANGER_LVL)
        val content = mapOf("user_input" to artifactName)

        val req: HttpRequest = httpTransport.createRequestFactory()
            .buildPostRequest(url, JsonHttpContent(GsonFactory.getDefaultInstance(), content))

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

    override fun validateDescription(reasonToSave: String): Boolean {
        val url = GenericUrl(DESCRIPTION_VALIDATION)
        val content = mapOf("reason_to_save" to reasonToSave)

        val req: HttpRequest = httpTransport.createRequestFactory()
            .buildPostRequest(url, JsonHttpContent(GsonFactory.getDefaultInstance(), content))

        val response: HttpResponse = req.execute()
        val responseText = response.parseAsString()
        response.disconnect()
        return responseText.toBoolean()
    }

    override fun addBannedUser(userAccountName: String): Boolean {
        val url = GenericUrl(BAN_USER)
        val content = mapOf("user_account_name" to userAccountName)

        val req: HttpRequest = httpTransport.createRequestFactory()
            .buildPostRequest(url, JsonHttpContent(GsonFactory.getDefaultInstance(), content))

        val response: HttpResponse = req.execute()
        response.disconnect()
        return response.statusCode == 200
    }

    override fun addBannedWord(word: String): Boolean {
        val url = GenericUrl(BAN_WORD)
        val content = mapOf("user_input" to word)

        val req: HttpRequest = httpTransport.createRequestFactory()
            .buildPostRequest(url, JsonHttpContent(GsonFactory.getDefaultInstance(), content))

        val response: HttpResponse = req.execute()
        response.disconnect()
        return response.statusCode == 200
    }


    /**
     * Локальный класс для удобного маппинга данных с сервера
     */
    private data class ArtifactDataFromAiServer(
        val Name: String,
        val Lvl: String
    )
}