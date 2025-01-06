package lab.`is`.bank.controller.export

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v0/export/deposit")
class ExportDepositController {


    @GetMapping("/XLSX")
    fun getDepositsXLSX() : String {
        return "XLSX"
    }
}