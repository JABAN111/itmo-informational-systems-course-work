package lab.`is`.bank.authorization.database.repository

import lab.`is`.bank.authorization.database.entity.Staff
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StaffRepository : JpaRepository<Staff, UUID> {
}