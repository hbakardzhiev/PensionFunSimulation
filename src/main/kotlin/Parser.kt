import models.Gender
import org.apache.poi.ss.usermodel.*
import java.time.ZoneId

fun Sheet.mortalityRateMaker(): Map<Int, Map<Int, Double>> {
    return drop(1).map { row ->
        val cell = row.getCell(0)
        val age = cell.numericCellValue.toInt()
        val ratesForAge: Map<Int, Double> = row.drop(1).mapIndexed { index, cell ->
            val rate = cell.numericCellValue
            val year = getRow(0).getCell(index + 1).numericCellValue.toInt()
            year to rate
        }.toMap()
        age to ratesForAge
    }.toMap()
}

fun Sheet.parseInterest(): Map<Int, Double> {
    return drop(2).filter { x -> x.getCell(0) != null }
        .map { row ->
        val c = row.getCell(0)
        val year = c.numericCellValue.toInt()
        val rate = row.getCell(1).numericCellValue
        year to rate
    }.toMap()
}

private fun Row.findColumnIndex(field: String): Int? {
    for (cell in this.iterator()) {
        if (cell.cellType == CellType.STRING && cell.stringCellValue == field)
            return cell.getColumnIndex()
    }
    return null
}

fun Sheet.parseConfiguration(mortalityMale: Map<Int, Map<Int, Double>>, mortalityFemale: Map<Int, Map<Int, Double>>): ConfigurationExcel {
    val calculationDateRow = getRow(1)
    val ageRow = getRow(2)
    val monthRow = getRow(3)
    val genderRow = getRow(4)
    val pensionRow = getRow(7)
    val manBeforeStartRow = getRow(17)
    val femaleBeforeStartRow = getRow(18)
    val manAfterStartRow = getRow(19)
    val femaleAfterStartRow = getRow(20)

    val manAfterStart = manAfterStartRow.getCell(1).numericCellValue
    val femaleAfterStart = femaleAfterStartRow.getCell(1).numericCellValue
    val manBeforeStart = manBeforeStartRow.getCell(1).numericCellValue
    val femaleBeforeStart = femaleBeforeStartRow.getCell(1).numericCellValue

    val gender = Gender.valueOf(genderRow.getCell(1).stringCellValue)
    val calculationDate = calculationDateRow.getCell(1).dateCellValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val age = ageRow.getCell(1).numericCellValue.toInt()
    val month = monthRow.getCell(1).numericCellValue.toInt()
    val pensionYear = pensionRow.getCell(1).numericCellValue.toInt()
    val pensionMonth = pensionRow.getCell(2).numericCellValue.toInt()

    return ConfigurationExcel(
        calculationDate, age, month, pensionYear, pensionMonth, gender,
        manBeforeStart, femaleBeforeStart, manAfterStart, femaleAfterStart,
        mortalityMale, mortalityFemale
    )
}