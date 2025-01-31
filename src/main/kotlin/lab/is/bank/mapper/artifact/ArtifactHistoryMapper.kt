package lab.`is`.bank.mapper.artifact

import lab.`is`.bank.database.entity.artifact.ArtifactHistory
import lab.`is`.bank.dto.artifact.ArtifactHistoryDto
import lab.`is`.bank.mapper.ClientMapper

class ArtifactHistoryMapper {
    companion object {
        fun toEntity(dto: ArtifactHistoryDto): ArtifactHistory{
            val result = ArtifactHistory()
            result.clientsHistory = dto.clientsHistory
            result.reasonToSave = dto.reasonToSave
            return result
        }
    }
}