package lab.`is`.bank.deposit.controller

import lab.`is`.bank.artifact.service.interfaces.ExportArtifactService
import lab.`is`.bank.authorization.service.impl.StaffServiceImpl
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
class ExportDepositController(
    private val exportDepositService: ExportDepositService,
    private val staffService: StaffServiceImpl,
) {


    @GetMapping("/xlsx")
    fun getDepositsXLSX(
        @RequestParam("accountId") accountId: UUID,
        @RequestParam("types") types: Array<String>
    ): ResponseEntity<ByteArray> {
        val data = exportDepositService.exportDepositsXLSX(accountId, types)

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=deposit_export.xlsx")
        headers.add(HttpHeaders.CONTENT_TYPE, "text/xlsx")
        headers.add(HttpHeaders.CONTENT_LENGTH, data.size.toString())

        return ResponseEntity(data, headers, HttpStatus.OK)
    }

    @GetMapping("/csv")
    fun getDepositsCSV(
        @RequestParam("accountId") accountId: UUID,
        @RequestParam("types") types: Array<String>
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
    fun getDepositsPdf(
        @RequestParam("accountId") accountId: UUID,
        @RequestParam("types") types: Array<String>
    ): ResponseEntity<ByteArray> {
        val data = exportDepositService.exportDepositsPdf(accountId, types)

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=deposit_export.pdf")
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf")
        headers.add(HttpHeaders.CONTENT_LENGTH, data.size.toString())

        return ResponseEntity(data, headers, HttpStatus.OK)
    }

}