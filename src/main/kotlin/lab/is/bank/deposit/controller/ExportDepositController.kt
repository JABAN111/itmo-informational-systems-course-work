package lab.`is`.bank.deposit.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import lab.`is`.bank.deposit.service.interfaces.ExportDepositService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v0/export/deposit")
@Tag(name = "Deposit Export", description = "Export deposit data in various formats")
class ExportDepositController(
    private val exportDepositService: ExportDepositService,
) {
    @GetMapping("/xlsx")
    @Operation(summary = "Export deposits to XLSX", description = "Exports deposit transactions to Excel format")
    fun getDepositsXLSX(
        @Parameter(description = "Deposit account ID") @RequestParam("accountId") accountId: UUID,
        @Parameter(description = "Transaction types to export") @RequestParam("types") types: Array<String>,
    ): ResponseEntity<ByteArray> {
        val data = exportDepositService.exportDepositsXLSX(accountId, types)

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=deposit_export.xlsx")
        headers.add(HttpHeaders.CONTENT_TYPE, "text/xlsx")
        headers.add(HttpHeaders.CONTENT_LENGTH, data.size.toString())

        return ResponseEntity(data, headers, HttpStatus.OK)
    }

    @GetMapping("/csv")
    @Operation(summary = "Export deposits to CSV", description = "Exports deposit transactions to CSV format")
    fun getDepositsCSV(
        @Parameter(description = "Deposit account ID") @RequestParam("accountId") accountId: UUID,
        @Parameter(description = "Transaction types to export") @RequestParam("types") types: Array<String>,
    ): ResponseEntity<ByteArray> {
        println(types)
        val data = exportDepositService.exportDepositsCSV(accountId, types)

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=deposit_export.csv")
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv")
        headers.add(HttpHeaders.CONTENT_LENGTH, data.size.toString())

        return ResponseEntity(data, headers, HttpStatus.OK)
    }

    @GetMapping("/pdf")
    @Operation(summary = "Export deposits to PDF", description = "Exports deposit transactions to PDF format")
    fun getDepositsPdf(
        @Parameter(description = "Deposit account ID") @RequestParam("accountId") accountId: UUID,
        @Parameter(description = "Transaction types to export") @RequestParam("types") types: Array<String>,
    ): ResponseEntity<ByteArray> {
        val data = exportDepositService.exportDepositsPdf(accountId, types)

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=deposit_export.pdf")
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf")
        headers.add(HttpHeaders.CONTENT_LENGTH, data.size.toString())

        return ResponseEntity(data, headers, HttpStatus.OK)
    }
}
