package lab.`is`.bank.artifact.dto

import lab.`is`.bank.authorization.database.entity.Client
import lab.`is`.bank.authorization.dto.ClientDto
import lab.`is`.bank.authorization.dto.StaffDto
import java.sql.Timestamp
import java.util.*

data class ArtifactDto(
    val name: String,
    val magicalProperty: MagicalPropertyDto? = null,
    val currentClient: ClientDto? = null,
    val artifactHistory: ArtifactHistoryDto? = null,
)

data class ArtifactHistoryDto(
    var lastClient: ClientDto? = null,
    var clientsHistory: MutableList<Client> = mutableListOf(),
    var reasonToSave: String = "",
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
    val keyValue: String,
    val giver: StaffDto
)

data class ArtifactExportData(
    val artifactId: UUID,
    val artifactName: String,
    val createdDate: Timestamp,
    val ownerPassportId: String?,
    val magicalDangerLevel: String?,
    val lastChangeDate: Timestamp?,
    val lastReasonToSave: String?
)