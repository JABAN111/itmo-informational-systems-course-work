package lab.`is`.bank.services.export

import java.util.*

interface ExportDepositService {
    fun exportDepositsXLSX(accountId: UUID, operations: Array<String>) : ByteArray
    fun exportDepositsCSV(accountId: UUID, operations: Array<String>) : ByteArray
    fun exportDepositsPdf(accountId: UUID, operations: Array<String>) : ByteArray
}
interface ExportArtifactService {
    fun exportArtifactsXLSX(someOwner: String?, someMagicProperty: List<String>?) : ByteArray
    fun exportArtifactsCSV(someOwner: String?, someMagicProperty: List<String>?) : ByteArray
    fun exportArtifactsPdf(someOwner: String?, someMagicProperty: List<String>?) : ByteArray
}