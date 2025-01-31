package lab.`is`.bank.dto.artifact

import lab.`is`.bank.database.entity.Client
import lab.`is`.bank.dto.ClientDto
import java.util.*

data class ArtifactDto(
    val name: String,
    val magicalProperty: MagicalPropertyDto? = null,
    val currentClient: ClientDto? = null,
    val artifactHistory: ArtifactHistoryDto? = null,
)

data class ArtifactHistoryDto(
    var lastClient: ClientDto? = null,
    var clientsHistory: MutableList<ClientDto> = mutableListOf()
)

data class MagicalPropertyDto(
    val dangerLevel: String = ""
)

data class ArtifactStorageDto(
    val artifact: ArtifactDto,
)

data class KeyDto(
    val artifactStorage: ArtifactStorageDto,
    val client: ClientDto,
    val keyValue: UUID,
)