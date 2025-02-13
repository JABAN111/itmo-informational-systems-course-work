package lab.`is`.bank.artifact.database.repository

import jakarta.persistence.Tuple
import lab.`is`.bank.artifact.database.entity.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ArtifactStorageRepository : JpaRepository<ArtifactStorage, UUID> {
    fun findByUuid(uuid: UUID): ArtifactStorage?

    @Query(nativeQuery = true, value = "select * from artifact_storage where artifact_name = :artifactName")
    fun findByArtifactName(
        @Param("artifactName") artifactName: String,
    ): Map<String, Tuple>
}

@Repository
interface KeyRepository : JpaRepository<Key, UUID> {
    fun findByClientPassportID(clientPassportId: String): List<Key>

    fun deleteKeyByArtifactStorage(artifactStorage: ArtifactStorage)

    fun deleteKeyByClientPassportID(clientPassport: String)
}

@Repository
interface ArtifactRepository : JpaRepository<Artifact, String> {
    fun findByName(artifactName: String): Artifact?

    fun deleteByName(artifactName: String)

    @Query(
        """
    SELECT * 
    FROM get_filtered_artifacts(
        :someOwner,
        STRING_TO_ARRAY(:someMagicProperties, ',') 
    )
    """,
        nativeQuery = true,
    )
    fun getFilteredArtifacts(
        @Param("someOwner") someOwner: String?,
        @Param("someMagicProperties") someMagicProperties: String?,
    ): List<Tuple>
}

@Repository
interface ArtifactHistoryRepository : JpaRepository<ArtifactHistory, UUID> {
    fun findArtifactHistoriesByArtifact(artifact: Artifact): ArtifactHistory?

    fun deleteByArtifactName(artifactName: String)
}
