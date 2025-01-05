package lab.`is`.bank.database.repository.depositManagement.transaction

import lab.`is`.bank.database.entity.depositManagement.transaction.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TransactionRepository : JpaRepository<Transaction, UUID> {
}