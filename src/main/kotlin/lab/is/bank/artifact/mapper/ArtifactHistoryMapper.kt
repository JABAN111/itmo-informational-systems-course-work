package lab.`is`.bank.artifact.mapper

import lab.`is`.bank.artifact.database.entity.ArtifactHistory
import lab.`is`.bank.artifact.dto.ArtifactHistoryDto

class ArtifactHistoryMapper {
    companion object {
        fun toEntity(dto: ArtifactHistoryDto): ArtifactHistory {
            val result = ArtifactHistory()
            result.clientsHistory = dto.clientsHistory
            result.reasonToSave = dto.reasonToSave
            return result
        }
    }
}