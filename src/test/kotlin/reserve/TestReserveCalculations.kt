package reserve

import ConfigurationExcel
import date
import kastromen
import models.FinalParticipant
import models.Gender
import mortalityRateMaker
import org.junit.jupiter.api.Test
import parseInterest
import workbook
import java.time.LocalDate
import kotlin.test.assertEquals

internal class TestReserveCalculations {

    private val mortalityM = workbook.getSheet("Collectief 2013 M").mortalityRateMaker()
    private val mortalityV = workbook.getSheet("Collectief 2013 V").mortalityRateMaker()

    private val interest = workbook.getSheet("Interest rates").parseInterest()
    private val startDate = LocalDate.of(2020, 1, 1)

    @Test
    fun `Male 68 retired ingOp and op`() {

        val result = kastromen(
            configuration = ConfigurationExcel(
                startDate, 68, 1, 67, 1,
                Gender.Man, 0.0, 0.0, 0.0, 0.0, mortalityM,
                mortalityV
            ),
            interestWorksheet = interest
        )

        val first = result.values.first()

        assertEquals(
            FinalParticipant(
                first.id,
                Gender.Man,
                birthDate = date.toString(),
                ingOp = 0.08313106796116504,
                op = 0.08313106796116504,
                startDate = startDate.toString()
            ),
            first
        )

    }

    @Test
    fun `Male 67 not retired ingOp and op`() {

        val result = kastromen(
            configuration = ConfigurationExcel(
                startDate, 67, 1, 68, 1,
                Gender.Man, 0.0, 0.0, 0.0, 0.0, mortalityM, mortalityV
            ),
            interestWorksheet = interest
        )
        val first = result.values.first()

        assertEquals(
            FinalParticipant(first.id, Gender.Man, 0.08313106796116504, 0.08313106796116504, date.toString(),
            startDate = startDate.toString()),
            first
        )
    }

    @Test
    fun `Female 68 retired ingOp and op`() {
        val result = kastromen(
            configuration = ConfigurationExcel(
                startDate, 68, 1, 67, 1,
                Gender.Vrouw, 0.0, 0.0, 0.0, 0.0, mortalityM, mortalityV
            ),
            interestWorksheet = interest
        )
        val first = result.values.first()

        assertEquals(
            FinalParticipant(
                first.id,
                Gender.Vrouw,
                0.08313106796116504,
                0.08313106796116504,
                date.toString(),
                startDate = startDate.toString()
            ), first
        )
    }

    @Test
    fun `Female 67 not retired ingOp and op`() {
        val result = kastromen(
            configuration = ConfigurationExcel(
                startDate, 67, 1, 68, 1,
                Gender.Vrouw, 0.0, 0.0, 0.0, 0.0, mortalityM,
                mortalityV
            ),
            interestWorksheet = interest
        )
        val first = result.values.first()

        assertEquals(
            FinalParticipant(
                first.id,
                Gender.Vrouw,
                0.08313106796116504,
                0.08313106796116504,
                date.toString(),
                startDate = startDate.toString()
            ), first
        )
    }
}