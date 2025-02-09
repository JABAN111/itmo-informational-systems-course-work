package lab.`is`.bank.artifact.mapper

import lab.`is`.bank.artifact.database.entity.ArtifactHistory
import lab.`is`.bank.artifact.dto.ArtifactHistoryDto

class ArtifactHistoryMapper {
    companion object {
        fun toEntity(dto: ArtifactHistoryDto): ArtifactHistory {
            return ArtifactHistory().apply {
                this.clientsHistory = dto.clientsHistory
                this.reasonToSave = dto.reasonToSave
                this.artifact = dto.artifact?.let { ArtifactMapper.toEntity(it) }!!
            }
        }
    }
}