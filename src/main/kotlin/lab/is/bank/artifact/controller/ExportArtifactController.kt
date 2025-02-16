package lab.`is`.bank.artifact.controller

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
class ExportArtifactController(
    private val exportArtifactService: ExportArtifactService,
) {
    @GetMapping("/csv")
    fun getArtifactCsv(
        @RequestParam("accountId", required = false) someOwner: String?,
        @RequestParam("types", required = false) someMagicProperty: List<String>?,
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
    fun getArtifactPdf(
        @RequestParam("accountId", required = false) someOwner: String?,
        @RequestParam("types", required = false) someMagicProperty: List<String>?,
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
    fun getArtifactXlsx(
        @RequestParam("accountId", required = false) accountId: String?,
        @RequestParam("types", required = false) types: List<String>?,
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
