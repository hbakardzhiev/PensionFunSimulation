import discountCurve.DiscountRate
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.math.abs
import kotlin.test.assertEquals

class DFTest {

    private val maturity = mapOf(
        LocalDate.of(2000, 1, 3) to 3.5,
        LocalDate.of(2000, 1, 4) to 3.0
    )

    private val interest = mapOf(
        3 to 0.035,
        4 to 0.03,
        5 to 0.03
    )

    private val df = DF()
    private val dfRate = DiscountRate()

    @Test
    fun `Test DF floor`() {
        val ceil = df.genInterestFloor(
            maturity, interest
        )
        assertEquals(maturityFloor, ceil, "Floor interest does not calculate properly")
    }

    @Test
    fun `Test DF ceil`() {
        val ceil = df.genInterestCeiling(
            maturity, interest
        )
        assertEquals(maturityCeil, ceil, "Ceil interest does not calculate properly")
    }

    @Test
    fun `Test DF period`() {
        val period = dfRate.getDiscountRatePerDate(maturity, interest, LocalDate.of(2000,1,3))
        assert(period.equalsDelta(0.90181))
    }


    private fun Double.equalsDelta(other: Double) =
        abs(this - other) <= 0.000009
}