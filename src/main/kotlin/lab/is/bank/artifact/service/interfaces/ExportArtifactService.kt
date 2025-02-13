package lab.`is`.bank.artifact.service.interfaces

interface ExportArtifactService {
    fun exportArtifactsXLSX(
        someOwner: String?,
        someMagicProperty: List<String>?,
    ): ByteArray

    fun exportArtifactsCSV(
        someOwner: String?,
        someMagicProperty: List<String>?,
    ): ByteArray

    fun exportArtifactsPdf(
        someOwner: String?,
        someMagicProperty: List<String>?,
    ): ByteArray
}
