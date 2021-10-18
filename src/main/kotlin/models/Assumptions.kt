package models

data class Assumptions(
    val age: Int, val month: Int, val date: String, val maleBeforeStart: Double,
    val maleAfterStart: Double, val femaleBeforeStart: Double,
    val femaleAfterStart: Double, val pensionYear: Int, val pensionMonth: Int,
    val sex: Gender
)
