package parser

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import parseInterest
import kotlin.test.assertEquals
import kotlin.test.assertFails


class TestInterestParser {

    private val workbook = XSSFWorkbook()
    private val sheet = workbook.createSheet()
    private lateinit var row : Row

    @BeforeEach
    fun setUp() {
        sheet.createRow(0)
        sheet.createRow(1)
        row = sheet.createRow(2)
    }

    @Test
    fun `Wrong format of cell`() {
        row.createCell(0).setCellValue("2")
        assertFails { sheet.parseInterest() }
    }

    @Test
    fun `Not all necessary cells filled correctly`() {
        row.createCell(0).setCellValue(2.0)
        row.createCell(1).setCellValue("2")
        assertFails { sheet.parseInterest() }
    }

    @Test
    fun `Check parsing correctly`() {
        row.createCell(0).setCellValue(1.0)
        row.createCell(1).setCellValue(1.0)

        val resultInterest = sheet.parseInterest()
        assertEquals(mutableMapOf(1 to 1.0), resultInterest)
    }
}