package lab.`is`.bank.database.entity.depositManagement

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lab.`is`.bank.database.entity.Client
import java.math.BigDecimal
import java.util.*

@Entity
class DepositAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var uuid: UUID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonIgnore
    lateinit var owner: Client


    @Enumerated(EnumType.STRING)
    lateinit var moneyType: MoneyType
    //update_ad & created_at сделать через слушателя

    lateinit var balance: BigDecimal

    lateinit var depositAccountName: String

}