package lab.`is`.bank.artifact.service.impl

import jakarta.persistence.Tuple
import jakarta.transaction.Transactional
import lab.`is`.bank.artifact.database.entity.Artifact
import lab.`is`.bank.artifact.database.repository.ArtifactRepository
import lab.`is`.bank.artifact.dto.ArtifactDto
import lab.`is`.bank.artifact.dto.ArtifactExportData
import lab.`is`.bank.artifact.exception.ArtifactAlreadySaved
import lab.`is`.bank.artifact.mapper.ArtifactMapper
import lab.`is`.bank.artifact.service.interfaces.ArtifactService
import org.springframework.stereotype.Service
import java.sql.Timestamp
import org.postgresql.util.PGobject
import java.text.SimpleDateFormat

@Service
@Transactional
class ArtifactServiceImpl(
    private val artifactRepository: ArtifactRepository,
) : ArtifactService {
    override fun save(artifact: Artifact): Artifact {
        val oldArtifact = artifactRepository.findByName(artifact.name)
        if (oldArtifact != null && oldArtifact.isStored) {
            throw ArtifactAlreadySaved("Artifact already in storage")
        }
        return artifactRepository.save(artifact)
    }

    override fun save(artifactDto: ArtifactDto): Artifact {
        val oldArtifact = artifactRepository.findByName(artifactDto.name)
        if (oldArtifact != null && oldArtifact.isStored) {
            throw ArtifactAlreadySaved("Artifact already in storage")
        }
        val artifactEntity = ArtifactMapper.toEntity(artifactDto).apply { this.isStored = true }

        return save(artifactEntity)
    }

    override fun delete(artifactName: String) {
        artifactRepository.deleteByName(artifactName)
    }

    override fun getDataForExport(
        someOwner: String?,
        someMagicProperty: List<String>?,
    ): List<ArtifactExportData> {
        val magicPropertiesArray = someMagicProperty?.toTypedArray()
        return mapTuplesToArtifactExportData(artifactRepository.getFilteredArtifacts(someOwner, magicPropertiesArray))
    }

    override fun getArtifact(artifactName: String): Artifact? = artifactRepository.findByName(artifactName)

    private fun mapTuplesToArtifactExportData(tuples: List<Tuple>): List<ArtifactExportData> {
        val result = mutableListOf<ArtifactExportData>()

        tuples.map { tuple ->
            val artifactForExport = getArtifactExport(getStringFromPGobject(tuple.get(0)))
            result.add(artifactForExport)

        }
        return result
    }



    private fun getStringFromPGobject(value: Any?): String {
        return if (value is PGobject) {
            value.value ?: ""
        } else {
            value as? String ?: ""
        }
    }

    private fun getArtifactExport(str: String): ArtifactExportData {
        val data = str.trim('(').trim(')').split(",")

        val artifactName = data[0]
        val createdDate = convertStringToTimestamp(data[1].trim('"'))
        val owner = data[2]
        val magicProperty = data[3]
        val lastModifiedDate = convertStringToTimestamp(data[4].trim('"'))
        val lastReasonToSave = data[5]

        val artifactExportData: ArtifactExportData = ArtifactExportData(
            artifactName = artifactName,
            createdDate = createdDate!!,
            ownerPassportId = owner,
            magicalDangerLevel = magicProperty,
            lastChangeDate = lastModifiedDate,
            lastReasonToSave = lastReasonToSave,
        )
        return artifactExportData
    }

    fun convertStringToTimestamp(dateString: String): Timestamp? {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        return try {
            val parsedDate = format.parse(dateString)
            Timestamp(parsedDate.time)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}
