import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.ceil
import kotlin.math.floor

internal fun genMaturity(configuration: ConfigurationExcel):
                Map<LocalDate, Double> {
    val localDate = configuration.date
    var dateDiscount =  localDate.plusMonths(1)
    val maturityMap = mutableMapOf<LocalDate, Double>()
    var dateIndex = localDate
    var diff = ChronoUnit.MONTHS.between(localDate, dateDiscount)

    var maturity = diff / 12.0
    val upperBoundary = 2120
    while (dateIndex.year <= upperBoundary) {
        maturityMap.put(dateIndex, maturity)
        dateIndex = dateIndex.plusMonths(1)
        dateDiscount = dateDiscount.plusMonths(1)
        diff = ChronoUnit.MONTHS.between(localDate, dateDiscount)
        maturity = diff / 12.0
    }

    return maturityMap
}

class DF {

    fun genInterestFloor(maturityMap: Map<LocalDate, Double>, interest: Map<Int, Double>):
            Map<LocalDate, Double> {
        val resultMap = mutableMapOf<LocalDate, Double>()

        maturityMap.forEach { (date, value) ->
            resultMap.put(date, interest[floor(value).toInt()]!!)
        }

        return resultMap
    }

    fun genInterestCeiling(maturityMap: Map<LocalDate, Double>, interest: Map<Int, Double>):
            Map<LocalDate, Double> {

        val resultMap = mutableMapOf<LocalDate, Double>()

        maturityMap.forEach { (date, value) ->
            resultMap.put(date, interest[ceil(value).toInt()]!!)
        }

        return resultMap
    }

}