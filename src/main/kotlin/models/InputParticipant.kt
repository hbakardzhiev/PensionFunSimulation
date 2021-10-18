package models

import java.time.LocalDate

data class InputParticipant(val id: String, val birthdate: LocalDate, val gender: Gender)
