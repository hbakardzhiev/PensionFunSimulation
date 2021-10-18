package parser

import mortalityRateMaker
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TestMortalityParser {

    private val workbook = XSSFWorkbook()
    private val sheet = workbook.createSheet()
    private val row = sheet.createRow(1)

    @BeforeEach
    fun setUp() {
        sheet.createRow(0).createCell(1).setCellValue(0.0)
    }

    @Test
    fun `Wrong format of cell`() {
        row.createCell(0).cellType = CellType.STRING
        assertFails { sheet.mortalityRateMaker() }
    }

    @Test
    fun `Not all necessary cells filled correctly`() {
        row.createCell(0).setCellValue(1.0)
        val yearRow = sheet.createRow(2)
        yearRow.createCell(0)
        yearRow.createCell(1).setCellValue("2")

        assertFails { sheet.mortalityRateMaker() }
    }

    @Test
    fun `Check parsing correctly`() {
        row.createCell(0).setCellValue(1.0)
        val yearRow = sheet.createRow(2)
        yearRow.createCell(0).setCellValue(2020.0)
        yearRow.createCell(1).setCellValue(2021.0)

        val result = sheet.mortalityRateMaker()

        assertEquals(
            mapOf(
                1 to emptyMap(),
                2020 to mapOf(0 to 2021.0)
            ), result
        )

    }
}