import models.Gender
import java.time.LocalDate

data class ConfigurationExcel(
    val date: LocalDate, val age: Int, val month: Int,
    var pensionYear: Int, val pensionMonth: Int,
    val sex: Gender, val maleBeforeStart: Double,
    val femaleBeforeStart: Double, val maleAfterStart: Double,
    val femaleAfterStart: Double, val maleMortality: Map<Int, Map<Int, Double>>,
    val femaleMortality: Map<Int, Map<Int, Double>>
)