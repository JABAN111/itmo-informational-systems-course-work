package lab.`is`.bank.database.repository

import lab.`is`.bank.database.entity.Staff
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StaffRepository : JpaRepository<Staff, UUID> {
}