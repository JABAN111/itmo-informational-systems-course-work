package lab.`is`.bank.deposit.service.interfaces

import java.util.*

interface ExportDepositService {
    fun exportDepositsXLSX(
        accountId: UUID,
        operations: Array<String>,
    ): ByteArray

    fun exportDepositsCSV(
        accountId: UUID,
        operations: Array<String>,
    ): ByteArray

    fun exportDepositsPdf(
        accountId: UUID,
        operations: Array<String>,
    ): ByteArray
}
