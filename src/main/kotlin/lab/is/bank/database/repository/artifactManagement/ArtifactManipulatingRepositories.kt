package lab.`is`.bank.database.repository.artifactManagement

import jakarta.persistence.Tuple
import lab.`is`.bank.database.entity.Client
import lab.`is`.bank.database.entity.artifact.*
import lab.`is`.bank.dto.ArtifactExportData
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
interface ArtifactRepository : JpaRepository<Artifact, UUID> {
    fun findByUuid(artifactId: UUID): Artifact?

    fun findByName(artifactName: String): Artifact?

    @Query(
        """
    SELECT * 
    FROM get_filtered_artifacts(
        :someOwner,
        STRING_TO_ARRAY(:someMagicProperties, ',') -- Преобразуем строку в массив
    )
    """,
        nativeQuery = true
    )
    fun getFilteredArtifacts(
        @Param("someOwner") someOwner: String?,
        @Param("someMagicProperties") someMagicProperties: String?
    ): List<Tuple>
//    @Query(
//        """
//        SELECT *
//        FROM get_filtered_artifacts(
//            :someOwner,
//            :someMagicProperty
//        )
//        """,
//        nativeQuery = true
//    )
//    fun getFilteredArtifacts(
//        @Param("someOwner") someOwner: String?,
//        @Param("someMagicProperty") someMagicProperty: String?
//    ): List<ArtifactExportData>
}

@Repository
interface ArtifactHistoryRepository : JpaRepository<ArtifactHistory, UUID>

@Repository
interface MagicalPropertyRepository : JpaRepository<MagicalProperty, UUID> {
}