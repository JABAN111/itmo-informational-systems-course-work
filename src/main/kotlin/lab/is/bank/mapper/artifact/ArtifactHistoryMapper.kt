package lab.`is`.bank.mapper.artifact

import lab.`is`.bank.database.entity.Client
import lab.`is`.bank.database.entity.artifact.ArtifactHistory
import lab.`is`.bank.dto.ClientDto
import lab.`is`.bank.dto.artifact.ArtifactHistoryDto
import lab.`is`.bank.mapper.ClientMapper

class ArtifactHistoryMapper {
    companion object {
        fun toEntity(dto: ArtifactHistoryDto): ArtifactHistory{
            val result = ArtifactHistory()
            val tmp = mutableListOf<Client>()

            for (clientDto in dto.clientsHistory){
                tmp.add(ClientMapper.toEntity(clientDto))
            }
            result.clientsHistory = tmp

            return result
        }
    }
}