package lab.`is`.bank.services.export

import java.util.*

interface ExportDepositService {
    fun exportDepositsXLSX(accountId: UUID, operations: Array<String>) : ByteArray
    fun exportDepositsCSV(accountId: UUID, operations: Array<String>) : ByteArray
    fun exportDepositsPdf(accountId: UUID, operations: Array<String>) : ByteArray
}
interface ExportArtifactService {
    fun exportArtifactsXLSX(artifactId: UUID) : ByteArray
    fun exportArtifactsCSV(artifactId: UUID) : ByteArray
    fun exportArtifactsPdf(artifactId: UUID) : ByteArray
}