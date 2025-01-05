package lab.`is`.bank.database.repository.depositManagement

import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DepositAccountRepository : JpaRepository<DepositAccount, UUID> {

    fun findDepositAccountsByOwnerPassportID(ownerPassportId: String): List<DepositAccount>
}