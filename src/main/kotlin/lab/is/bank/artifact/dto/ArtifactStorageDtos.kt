package lab.`is`.bank.artifact.dto

import lab.`is`.bank.authorization.database.entity.Client
import lab.`is`.bank.authorization.dto.ClientDto
import lab.`is`.bank.authorization.dto.StaffDto
import java.sql.Timestamp
import java.util.*

data class ArtifactDto(
    val name: String,
    var magicalProperty: MagicalPropertyDto? = null,
    var currentClient: ClientDto? = null,
)

data class ArtifactHistoryDto(
    var clientsHistory: MutableList<Client> = mutableListOf(),
    var reasonToSave: String = "",
    var artifact: ArtifactDto? = null
)

data class MagicalPropertyDto(
    val dangerLevel: String = "",
    var property: String = ""
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
    val artifactName: String,
    val createdDate: Timestamp,
    val ownerPassportId: String?,
    val magicalDangerLevel: String?,
    val lastChangeDate: Timestamp?,
    val lastReasonToSave: String?
)

data class RetrieveArtifactRequest(
    val passportID: String,
    val storageUuid: UUID
)

data class UpdateArtifactRequest(
    val name: String,
    val newDangerLevel: String,
)