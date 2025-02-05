package lab.`is`.bank.deposit.service.impl

import com.itextpdf.kernel.colors.DeviceGray
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import com.opencsv.CSVWriter
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.util.*

import lab.`is`.bank.deposit.service.interfaces.ExportDepositService
import lab.`is`.bank.deposit.service.interfaces.TransactionService


@Service
class ExportDepositServiceImpl(
    val transactionService: TransactionService
) : ExportDepositService {

    override fun exportDepositsXLSX(accountId: UUID, operations: Array<String>): ByteArray {
        val listData = transactionService.getDataForExport(accountId, operations)

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Deposits")

        val headerRow = sheet.createRow(0)
        val headers = arrayOf("moneyType", "accountID", "transactionAmount", "transactionCreate")
        headers.forEachIndexed { index, header ->
            headerRow.createCell(index).setCellValue(header)
        }

        listData.forEachIndexed { rowIndex, deposit ->
            val row = sheet.createRow(rowIndex + 1)
            row.createCell(0).setCellValue(deposit.moneyType)
            row.createCell(1).setCellValue(deposit.accountID.toString())
            row.createCell(2).setCellValue(deposit.transactionAmount.toDouble())
            row.createCell(3).setCellValue(deposit.transactionCreate)
        }

        headers.indices.forEach { sheet.autoSizeColumn(it) }

        val byteArrayOutputStream = ByteArrayOutputStream()
        workbook.write(byteArrayOutputStream)
        workbook.close()

        return byteArrayOutputStream.toByteArray()
    }

    override fun exportDepositsCSV(accountId: UUID, operations: Array<String>): ByteArray {
        val listData = transactionService.getDataForExport(accountId, operations)

        val byteArrayOutputStream = ByteArrayOutputStream()
        val outputStreamWriter = OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8)
        val csvWriter = CSVWriter(outputStreamWriter)

        csvWriter.writeNext(arrayOf("moneyType", "accountID", "transactionAmount", "transactionCreate"))

        listData.forEach { deposit ->
            csvWriter.writeNext(
                arrayOf(
                    deposit.moneyType,
                    deposit.accountID.toString(),
                    deposit.transactionAmount.toString(),
                    deposit.transactionCreate
                )
            )
        }

        csvWriter.close()
        byteArrayOutputStream.close()

        return byteArrayOutputStream.toByteArray()
    }


    override fun exportDepositsPdf(accountId: UUID, operations: Array<String>): ByteArray {
        val listData = transactionService.getDataForExport(accountId, operations)

        val byteArrayOutputStream = ByteArrayOutputStream()
        val pdfWriter = PdfWriter(byteArrayOutputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        val table = Table(floatArrayOf(2f, 6f, 4f, 4f))
        table.setWidth(100f)

        val headers = arrayOf("moneyType", "accountID", "transactionAmount", "transactionCreate")
        headers.forEach { header ->
            val headerCell = Cell().add(Paragraph(header))
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
            table.addHeaderCell(headerCell)
        }

        listData.forEachIndexed { rowIndex, deposit ->
            val isEvenRow = rowIndex % 2 == 0
            val rowColor = if (isEvenRow) DeviceGray(0.9f) else null

            val cells = arrayOf(
                deposit.moneyType,
                deposit.accountID.toString(),
                deposit.transactionAmount.toString(),
                deposit.transactionCreate
            )

            cells.forEach { cellValue ->
                val cell = Cell().add(Paragraph(cellValue))
                    .setTextAlignment(TextAlignment.CENTER)
                rowColor?.let { cell.setBackgroundColor(it) }
                table.addCell(cell)
            }
        }

        document.add(table)

        document.close()

        return byteArrayOutputStream.toByteArray()
    }
}