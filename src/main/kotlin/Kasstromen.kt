import discountCurve.DiscountRate
import environment.generateUUID
import models.FinalParticipant

fun kastromen(
    configuration: ConfigurationExcel,
    interestWorksheet: Map<Int, Double>
): Map<String, FinalParticipant> {
    val yearFrac = 1 / 12.0

    val uopMarkovChain = genUOPMarkovChain(configuration)
    val opMarkovChain = genOpMarkovChain(configuration)
    val maturity = genMaturity(configuration)
    val discountRate = DiscountRate()

    val result = mutableMapOf<String, FinalParticipant>()
    opMarkovChain.forEach { (date, value) ->
        val op = uopMarkovChain.get(date)?.second ?: 0.0
        val df = discountRate.getDiscountRatePerDate(maturity, interestWorksheet, date)
        val id = generateUUID()

        result[id] = FinalParticipant(
            id = id,
            gender = configuration.sex,
            startDate = configuration.date.toString(),
            birthDate = date.toString(),
            op = op * yearFrac * df,
            ingOp = value * yearFrac * df
        )
    }

    return result
}

