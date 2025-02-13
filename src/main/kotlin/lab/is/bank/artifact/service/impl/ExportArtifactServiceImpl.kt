package lab.`is`.bank.artifact.service.impl

import com.itextpdf.kernel.colors.DeviceGray
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.opencsv.CSVWriter
import lab.`is`.bank.artifact.service.interfaces.ArtifactService
import lab.`is`.bank.artifact.service.interfaces.ExportArtifactService
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

@Service
class ExportArtifactServiceImpl(
    private val artifactService: ArtifactService,
) : ExportArtifactService {
    override fun exportArtifactsXLSX(
        someOwner: String?,
        someMagicProperty: List<String>?,
    ): ByteArray {
        val artifacts = artifactService.getDataForExport(someOwner, someMagicProperty)

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Artifacts")

        val headerRow = sheet.createRow(0)
        val headers =
            arrayOf(
                "artifactName",
                "createdDate",
                "ownerPassportId",
                "magicalDangerLevel",
                "lastChangeDate",
                "lastReasonToSave",
            )
        headers.forEachIndexed { index, header ->
            headerRow.createCell(index).setCellValue(header)
        }

        artifacts.forEachIndexed { rowIndex, artifact ->
            val row = sheet.createRow(rowIndex + 1)
            row.createCell(0).setCellValue(artifact.artifactName)
            row.createCell(1).setCellValue(artifact.createdDate.toString())
            row.createCell(2).setCellValue(artifact.ownerPassportId ?: "")
            row.createCell(3).setCellValue(artifact.magicalDangerLevel ?: "")
            row.createCell(4).setCellValue(artifact.lastChangeDate.toString())
            row.createCell(5).setCellValue(artifact.lastReasonToSave ?: "")
        }

        headers.indices.forEach { sheet.autoSizeColumn(it) }

        val byteArrayOutputStream = ByteArrayOutputStream()
        workbook.write(byteArrayOutputStream)
        workbook.close()

        return byteArrayOutputStream.toByteArray()
    }

    override fun exportArtifactsCSV(
        someOwner: String?,
        someMagicProperty: List<String>?,
    ): ByteArray {
        val artifacts = artifactService.getDataForExport(someOwner, someMagicProperty)

        val byteArrayOutputStream = ByteArrayOutputStream()
        val outputStreamWriter = OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8)
        val csvWriter = CSVWriter(outputStreamWriter)

        csvWriter.writeNext(
            arrayOf(
                "artifactId",
                "artifactName",
                "createdDate",
                "ownerPassportId",
                "magicalDangerLevel",
                "lastChangeDate",
                "lastReasonToSave",
            ),
        )

        artifacts.forEach { artifact ->
            csvWriter.writeNext(
                arrayOf(
                    artifact.artifactName,
                    artifact.createdDate.toString(),
                    artifact.ownerPassportId ?: "",
                    artifact.magicalDangerLevel ?: "",
                    artifact.lastChangeDate.toString(),
                    artifact.lastReasonToSave ?: "",
                ),
            )
        }

        csvWriter.close()
        byteArrayOutputStream.close()

        return byteArrayOutputStream.toByteArray()
    }

    override fun exportArtifactsPdf(
        someOwner: String?,
        someMagicProperty: List<String>?,
    ): ByteArray {
        val artifacts = artifactService.getDataForExport(someOwner, someMagicProperty)

        val byteArrayOutputStream = ByteArrayOutputStream()
        val pdfWriter = PdfWriter(byteArrayOutputStream)
        val pdfDocument = PdfDocument(pdfWriter)

        pdfDocument.defaultPageSize =
            com.itextpdf.kernel.geom.PageSize.A4
                .rotate()

        val document = Document(pdfDocument)

        val table = Table(floatArrayOf(2f, 6f, 4f, 4f, 4f, 4f, 4f))
        table.setWidth(100f)

        val headers =
            arrayOf(
                "artifactName",
                "createdDate",
                "ownerPassportId",
                "magicalDangerLevel",
                "lastChangeDate",
                "lastReasonToSave",
            )
        headers.forEach { header ->
            val headerCell =
                Cell()
                    .add(Paragraph(header))
                    .setBold()
            table.addHeaderCell(headerCell)
        }

        artifacts.forEachIndexed { rowIndex, artifact ->
            val isEvenRow = rowIndex % 2 == 0
            val rowColor = if (isEvenRow) DeviceGray(0.9f) else null

            val cells =
                arrayOf(
                    artifact.artifactName,
                    artifact.createdDate.toString(),
                    artifact.ownerPassportId ?: "",
                    artifact.magicalDangerLevel ?: "",
                    artifact.lastChangeDate.toString(),
                    artifact.lastReasonToSave ?: "",
                )

            cells.forEach { cellValue ->
                val cell = Cell().add(Paragraph(cellValue))
                rowColor?.let { cell.setBackgroundColor(it) }
                table.addCell(cell)
            }
        }

        document.add(table)
        document.close()

        return byteArrayOutputStream.toByteArray()
    }
}
