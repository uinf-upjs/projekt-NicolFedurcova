package sk.upjs.hackstock.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date
import java.util.UUID

@Entity(tableName = "user")
data class User (val name: String,
                 val surname: String,
                 val dateOfBirth: Date,
                 val country: String,
                 val city: String,
                 val status: String,
                 val occupation: String,
                 val annualIncome: Double,
                 val money: Double,
                 val score: Int): Serializable {

    @PrimaryKey
    var uuid: UUID = UUID.randomUUID()
}