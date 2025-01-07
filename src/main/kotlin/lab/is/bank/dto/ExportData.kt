package lab.`is`.bank.dto

import java.math.BigDecimal
import java.util.UUID

data class DepositExportData(
    val moneyType: String,
    val accountID: UUID,
    val transactionAmount: BigDecimal,
    val transactionCreate: String
)

data class ArtifactExportData(
    val artifactName: String
)