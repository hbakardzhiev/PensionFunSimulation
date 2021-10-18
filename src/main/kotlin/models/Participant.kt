package models

import java.time.LocalDate

data class Participant(
    val gender: Gender,
    val birthdate: LocalDate,
    val op: Double,
    val ingOp: Double,
    val time: LocalDate
)