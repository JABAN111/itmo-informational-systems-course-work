package lab.`is`.bank.artifact.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import lab.`is`.bank.artifact.service.interfaces.ExportArtifactService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v0/export/artifact")
@Tag(name = "Artifact Export", description = "Export artifact data in various formats")
class ExportArtifactController(
    private val exportArtifactService: ExportArtifactService,
) {
    @GetMapping("/csv")
    @Operation(summary = "Export artifacts to CSV", description = "Exports artifact data to CSV format")
    fun getArtifactCsv(
        @Parameter(description = "Account ID filter") @RequestParam("accountId", required = false) someOwner: String?,
        @Parameter(description = "Artifact types filter") @RequestParam("types", required = false) someMagicProperty: List<String>?,
    ): ResponseEntity<ByteArray> {
        val data =
            exportArtifactService.exportArtifactsCSV(
                someOwner = someOwner,
                someMagicProperty = someMagicProperty,
            )

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=artifact_export.csv")
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv")
        headers.add(HttpHeaders.CONTENT_LENGTH, data.size.toString())

        return ResponseEntity(data, headers, HttpStatus.OK)
    }

    @GetMapping("/pdf")
    @Operation(summary = "Export artifacts to PDF", description = "Exports artifact data to PDF format")
    fun getArtifactPdf(
        @Parameter(description = "Account ID filter") @RequestParam("accountId", required = false) someOwner: String?,
        @Parameter(description = "Artifact types filter") @RequestParam("types", required = false) someMagicProperty: List<String>?,
    ): ResponseEntity<ByteArray> {
        val data =
            exportArtifactService.exportArtifactsPdf(
                someOwner = someOwner,
                someMagicProperty = someMagicProperty,
            )

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=artifact_export.pdf")
        headers.add(HttpHeaders.CONTENT_TYPE, "text/pdf")
        headers.add(HttpHeaders.CONTENT_LENGTH, data.size.toString())

        return ResponseEntity(data, headers, HttpStatus.OK)
    }

    @GetMapping("/xlsx")
    @Operation(summary = "Export artifacts to XLSX", description = "Exports artifact data to Excel format")
    fun getArtifactXlsx(
        @Parameter(description = "Account ID filter") @RequestParam("accountId", required = false) accountId: String?,
        @Parameter(description = "Artifact types filter") @RequestParam("types", required = false) types: List<String>?,
    ): ResponseEntity<ByteArray> {
        val data =
            exportArtifactService.exportArtifactsXLSX(
                someOwner = accountId,
                someMagicProperty = types,
            )

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=artifact_export.xlsx")
        headers.add(HttpHeaders.CONTENT_TYPE, "text/xlsx")
        headers.add(HttpHeaders.CONTENT_LENGTH, data.size.toString())

        return ResponseEntity(data, headers, HttpStatus.OK)
    }
}
