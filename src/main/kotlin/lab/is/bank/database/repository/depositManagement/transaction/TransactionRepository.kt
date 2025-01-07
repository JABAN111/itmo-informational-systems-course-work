package lab.`is`.bank.database.repository.depositManagement.transaction

import jakarta.persistence.SqlResultSetMapping
import jakarta.persistence.Tuple
import lab.`is`.bank.database.entity.depositManagement.transaction.Transaction
import lab.`is`.bank.dto.DepositExportData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TransactionRepository : JpaRepository<Transaction, UUID> {

        @Query(
            """
        SELECT * 
        FROM get_account_transactions_multiple_types(
            :accountId,
            CAST(:types AS text[])
        )
        """,
            nativeQuery = true
        )
        fun getDataForReport(
            @Param("accountId") accountId: UUID,
            @Param("types") types: Array<String>
        ): List<Tuple>

}