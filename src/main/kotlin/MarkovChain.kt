import environment.InMemoryDatabase
import models.Gender
import java.time.LocalDate
import kotlin.math.pow

fun genUOPMarkovChain(configuration: ConfigurationExcel): Map<LocalDate, Pair<Boolean, Double>> {
    val deathRateMap = if (configuration.sex == Gender.Man) 1 else 0
    val ageReset = if (configuration.sex == Gender.Man) configuration.maleAfterStart else configuration.femaleAfterStart
    val monthlyMortRatePerDate = mutableMapOf<LocalDate, Double>()
    val aliveFractionMutableMap = mutableMapOf<LocalDate, Pair<Boolean, Double>>()
    val retired =
        checkIfRetired(configuration.pensionYear, configuration.pensionMonth, configuration.age, configuration.month)
    aliveFractionMutableMap.put(configuration.date, Pair(retired, 1.0))

    var date = configuration.date
    var ageDate = LocalDate.of(configuration.age, configuration.month, 1)

    val ageBoundary = configuration.age + 2000
    while (date.year != 2120) {
        date = date.plusMonths(1)
        val retired = checkIfRetired(
            configuration.pensionYear, configuration.pensionMonth,
            ageDate.year, ageDate.monthValue
        )
        val reset = getReset(configuration.sex, retired, configuration)
        val mortalityRisk = ageDate.year - reset
        val yearlyMortality: Double = if (mortalityRisk > 120) 1.0 else
            InMemoryDatabase.getMortalityForCurrSex(configuration)[mortalityRisk.toInt()]!![date.year]!!
        val diff = (1.0 - yearlyMortality).pow(1 / 12.0)
        val monthlyMortality = 1 - diff
        val beforeValue = aliveFractionMutableMap[date.minusMonths(1)]!!
        val parsed = if (beforeValue.first) beforeValue.second else 0.0
        aliveFractionMutableMap[date] = Pair(retired, parsed * (1 - monthlyMortality))
        ageDate = ageDate.plusMonths(1)
    }

    return aliveFractionMutableMap
}


fun genOpMarkovChain(configuration: ConfigurationExcel): Map<LocalDate, Double> {
    val terugstelling =
        if (configuration.sex == Gender.Man) configuration.maleAfterStart else configuration.femaleAfterStart
    val ageReset = if (configuration.sex == Gender.Man) configuration.maleAfterStart else configuration.femaleAfterStart
    val monthlyMortRatePerDate = mutableMapOf<LocalDate, Double>()

    val aliveFractionMutableMap = mutableMapOf<LocalDate, Double>()
    aliveFractionMutableMap.put(configuration.date, 1.0)

    var date = configuration.date
    var ageDate = LocalDate.of(configuration.age, configuration.month, 1)

    while (date.year != 2120) {
        date = date.plusMonths(1)
        val deathRisk = ageDate.year - terugstelling
        val yearMortality = getYearMortality(deathRisk, configuration, date)
        val monthlyMortality = 1 - (1 - yearMortality).pow(1 / 12.0)

        val beforeValue = aliveFractionMutableMap[date.minusMonths(1)]!!
        aliveFractionMutableMap[date] = beforeValue * (1 - monthlyMortality)
        ageDate = ageDate.plusMonths(1)
    }

    return aliveFractionMutableMap
}

fun getYearMortality(deathRisk: Double, configuration: ConfigurationExcel, date: LocalDate, year: Int = 2018): Double {
    return if (deathRisk > 120) 1.0 else InMemoryDatabase.getMortalityForCurrSex(configuration)[deathRisk.toInt()]!![date.year]!!
}

fun getReset(sex: Gender, retired: Boolean, configuration: ConfigurationExcel): Double {
    if (sex == Gender.Man && retired) {
        return configuration.maleAfterStart
    }
    if (sex == Gender.Vrouw && retired) {
        return configuration.femaleAfterStart
    }
    if (sex == Gender.Man && !retired) {
        return configuration.maleBeforeStart
    }
    if (sex == Gender.Vrouw && !retired) {
        return configuration.femaleBeforeStart
    }
    throw Exception()
}


private fun checkIfRetired(retirementAge: Int, retirementMonth: Int, ageYear: Int, ageMonth: Int): Boolean {
    if (ageYear > retirementAge) {
        return true
    }
    if (retirementAge == ageYear && retirementMonth <= ageMonth) {
        return true
    }
    return false
}