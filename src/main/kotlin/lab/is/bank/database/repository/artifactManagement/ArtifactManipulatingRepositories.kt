package lab.`is`.bank.database.repository.artifactManagement

import lab.`is`.bank.database.entity.artifact.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ArtifactStorageRepository : JpaRepository<ArtifactStorage, UUID>

@Repository
interface KeyRepository : JpaRepository<Key, UUID>

@Repository
interface ArtifactRepository : JpaRepository<Artifact, UUID> {
    fun findByUuid(artifactId: UUID): Artifact?
}

@Repository
interface ArtifactHistoryRepository : JpaRepository<ArtifactHistory, UUID>

@Repository
interface MagicalPropertyRepository : JpaRepository<MagicalProperty, UUID> {
}