package lab.`is`.bank.artifact.service.impl

import jakarta.persistence.Tuple
import jakarta.transaction.Transactional
import lab.`is`.bank.artifact.database.entity.Artifact
import lab.`is`.bank.artifact.database.repository.ArtifactRepository
import lab.`is`.bank.artifact.dto.ArtifactDto
import lab.`is`.bank.artifact.dto.ArtifactExportData
import lab.`is`.bank.artifact.mapper.ArtifactMapper
import lab.`is`.bank.artifact.exception.ArtifactAlreadySaved
import lab.`is`.bank.artifact.service.interfaces.ArtifactService
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*


@Service
@Transactional
class ArtifactServiceImpl(
    private val artifactRepository: ArtifactRepository
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


    override fun getDataForExport(someOwner: String?, someMagicProperty: List<String>?): List<ArtifactExportData> {
        val magicPropertiesString = someMagicProperty?.joinToString(",")
        return mapTuplesToArtifactExportData(artifactRepository.getFilteredArtifacts(someOwner, magicPropertiesString))
    }

    override fun getArtifact(artifactName: String): Artifact? {
        return artifactRepository.findByName(artifactName)
    }

    private fun mapTuplesToArtifactExportData(tuples: List<Tuple>): List<ArtifactExportData> {
        return tuples.map { tuple ->
            val artifactName = tuple.get("artifact_nam", String::class.java)
            val createdDate = tuple.get("created_date", Timestamp::class.java)
            val ownerPassportId = tuple.get("owner_passport_id", String::class.java)
            val magicalDangerLevel = tuple.get("magical_danger_level", String::class.java)
            val lastChangeDate = tuple.get("last_change_date", Timestamp::class.java)
            val lastReasonToSave = tuple.get("last_reason_to_save", String::class.java)

            ArtifactExportData(
                artifactName = artifactName,
                createdDate = createdDate,
                ownerPassportId = ownerPassportId,
                magicalDangerLevel = magicalDangerLevel,
                lastChangeDate = lastChangeDate,
                lastReasonToSave = lastReasonToSave
            )
        }
    }

}