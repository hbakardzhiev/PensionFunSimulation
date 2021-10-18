package discountCurve

import interest
import maturityCeil
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TestDiscountRate {
    @Test
    fun `Test Discount Rate for a certain date`() {
        val result = DiscountRate().getDiscountRatePerDate(maturityCeil, interest, LocalDate.of(2000,1,3))
        assertEquals(0.999126213592233, result)
    }

    @Test
    fun `Test Discount Rate error`() {
        assertFails { DiscountRate().getDiscountRatePerDate(maturityCeil, interest, LocalDate.of(2020,5,5)) }
    }
}