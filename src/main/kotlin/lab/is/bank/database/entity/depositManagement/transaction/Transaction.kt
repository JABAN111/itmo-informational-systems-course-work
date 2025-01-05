package lab.`is`.bank.database.entity.depositManagement.transaction

import jakarta.persistence.*
import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import java.math.BigDecimal
import java.util.*

@Entity
class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    var fromAccount: DepositAccount? = null

    /**
     * Может быть nullable, потому что в транзакцию попадает понятие "положить деньги на депозитный счёт" и подобные
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    var toAccount: DepositAccount? = null

    var amount: BigDecimal? = null

    @Enumerated(EnumType.STRING)
    var transactionStatus: TransactionStatus? = null

    @Enumerated(EnumType.STRING)
    var transactionType: TransactionType? = null

    var transactionDate: Date? = null


    @PrePersist
    fun creationDate(){
        transactionDate = Date()
    }
}