package lab.`is`.bank.artifact.database.repository

import jakarta.persistence.Tuple
import lab.`is`.bank.artifact.database.entity.*
import lab.`is`.bank.authorization.database.entity.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ArtifactStorageRepository : JpaRepository<ArtifactStorage, UUID>{
    fun findByUuid(uuid: UUID): ArtifactStorage?
}

@Repository
interface KeyRepository : JpaRepository<Key, UUID>{
    fun findByClient(client: Client): List<Key>
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
        nativeQuery = true
    )
    fun getFilteredArtifacts(
        @Param("someOwner") someOwner: String?,
        @Param("someMagicProperties") someMagicProperties: String?
    ): List<Tuple>
}

@Repository
interface ArtifactHistoryRepository : JpaRepository<ArtifactHistory, UUID>{
    fun findArtifactHistoriesByArtifact(artifact: Artifact): ArtifactHistory?
}

@Repository
interface MagicalPropertyRepository : JpaRepository<MagicalProperty, UUID> {
}