package com.example.finalobligatoriskkotlin.models

import com.squareup.moshi.Json
import java.io.Serializable
import java.util.Calendar

data class Person(
    @Json(name = "person_id") val id: Int,
    @Json(name = "user_id") val userId: String,
    val name: String,
    @Json(name = "birth_year") val birthYear: Int,
    @Json(name = "birth_month") val birthMonth: Int,
    @Json(name = "birth_day_of_month") val birthDayOfMonth: Int,
    val remarks: String? = null,
    @Json(name = "picture_url") val pictureUrl: String? = null
) : Serializable{

    val age: Int
        get() = calculateAge()

    public fun calculateAge(): Int {
        val today = Calendar.getInstance()
        val birthDate = Calendar.getInstance().apply {
            set(birthYear, birthMonth - 1, birthDayOfMonth) // month is 0-based
        }

        var age = today[Calendar.YEAR] - birthDate[Calendar.YEAR]

        if (today[Calendar.DAY_OF_YEAR] < birthDate[Calendar.DAY_OF_YEAR]) {
            age--  // Subtract a year if birthday hasn't occurred yet this year
        }

        return age
    }

    override fun toString(): String {
        return """
            Name: $name
            Age: $age
            Birthday: $birthDayOfMonth/$birthMonth/$birthYear
            Birth Year: $birthYear
            Birth Month: $birthMonth
            Birth Day of Month: $birthDayOfMonth
            Remarks: ${remarks ?: "None"}
        """.trimIndent()
    }
}
