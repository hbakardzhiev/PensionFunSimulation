package queries

import models.*
import java.time.Instant

interface Queries {
    fun getParticipant(viewer: Account?, id: String, timestamp: Instant?): FinalParticipant
    fun updateParticipant(viewer: Account?, id: String, gender: Gender, birthDate: String, context: Context): FinalParticipant
    fun getParticipants(viewer: Account?): List<FinalParticipant>
    fun updateInterest(viewer: Account?, age: Int, interest: Double, context: Context): Double
    fun deleteParticipant(viewer: Account?, id: String, context: Context): String
    fun getAssumptions(viewer: Account?): Assumptions
    fun getInterest(viewer: Account?, age: Int): Double
    fun updateAssumptions(
        viewer: Account?,
        assumptions: Assumptions, context: Context
    ) : Assumptions
    fun getMortalityRate(viewer: Account?, age: Int, year: Int): Double
    fun getAccessCodeForAccount(id: String, password: String): String?
    fun getEvents(): List<TransportEvent>
}