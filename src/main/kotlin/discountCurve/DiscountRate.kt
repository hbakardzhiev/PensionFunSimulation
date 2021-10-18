package discountCurve

import kotlin.math.ceil
import kotlin.math.floor
import java.time.LocalDate
import kotlin.math.pow

class DiscountRate {
    fun getDiscountRatePerDate(maturity: Map<LocalDate, Double>, interest: Map<Int, Double>, calculationDate: LocalDate): Double {
        val maturityVal = maturity[calculationDate]!!
        val intRateFloor = interest.getValue(floor(maturityVal).toInt() + 1)
        val intRateCeil = interest.getValue(ceil(maturityVal).toInt() + 1)
        val dfFloor = (1.0 + intRateFloor).pow(-floor(maturityVal))
        val dfCeil = (1.0 + intRateCeil).pow(-ceil(maturityVal))
        return (1 - maturityVal % 1.0) * dfFloor + (maturityVal % 1.0) * dfCeil
    }
}