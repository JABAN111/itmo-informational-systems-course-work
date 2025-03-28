package lab.`is`.bank.deposit.database.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lab.`is`.bank.authorization.database.entity.Client
import java.math.BigDecimal
import java.util.*

@Entity
class DepositAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonIgnore
    lateinit var owner: Client

    @Enumerated(EnumType.STRING)
    lateinit var moneyType: MoneyType

    lateinit var balance: BigDecimal

    lateinit var depositAccountName: String
}
