package lab.`is`.bank.authorization.mapper

import lab.`is`.bank.authorization.database.entity.Staff
import lab.`is`.bank.authorization.database.entity.StaffRole
import lab.`is`.bank.authorization.dto.StaffDto

class StaffMapper {
    companion object {
        fun toEntity(dto: StaffDto): Staff {
            val res = Staff()

            res.staffName = dto.staffName
            res.role = if (dto.role != null) StaffRole.ROLE_ARTIFACTER else null

            return res
        }
    }
}
