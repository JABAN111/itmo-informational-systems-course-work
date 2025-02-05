package lab.`is`.bank.deposit.database.repository.transaction

import jakarta.persistence.Tuple
import lab.`is`.bank.deposit.database.entity.transaction.Transaction
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