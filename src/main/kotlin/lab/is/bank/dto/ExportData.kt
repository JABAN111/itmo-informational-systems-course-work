package lab.`is`.bank.dto

import java.math.BigDecimal
import java.sql.Timestamp
import java.util.UUID

data class DepositExportData(
    val moneyType: String,
    val accountID: UUID,
    val transactionAmount: BigDecimal,
    val transactionCreate: String
)

data class ArtifactExportData(
    val artifactId: UUID,
    val artifactName: String,
    val createdDate: Timestamp,
    val ownerPassportId: String?,
    val magicalDangerLevel: String?,
    val lastChangeDate: Timestamp?,
    val lastReasonToSave: String?
)