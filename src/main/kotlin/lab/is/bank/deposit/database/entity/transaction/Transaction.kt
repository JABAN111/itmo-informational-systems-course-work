package lab.`is`.bank.deposit.database.entity.transaction

import jakarta.persistence.*
import lab.`is`.bank.authorization.database.entity.Staff
import lab.`is`.bank.deposit.database.entity.DepositAccount
import java.math.BigDecimal
import java.util.*

@Entity
class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var uuid: UUID? = null

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

    @ManyToOne
    var transaction: Staff? = null

    @PrePersist
    fun creationDate() {
        transactionDate = Date()
    }
}
