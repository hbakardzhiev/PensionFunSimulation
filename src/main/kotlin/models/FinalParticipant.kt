package models

data class FinalParticipant(
    val id: String,
    val gender: Gender,
    val op: Double? = null,
    val ingOp: Double? = null,
    var birthDate: String,
    val retirementAge: Int? = null,
    val startDate: String? = null,
    var employed: Boolean? = null,
    var salary: Int? = null,
    var streetName: String? = null,
    var houseNumber: Int? = null,
    var postalCode: String? = null,
    var country: String? = null
)