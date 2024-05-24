package sk.upjs.hackstock.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date
import java.util.UUID

@Entity(tableName = "user")
data class User (
//                 @PrimaryKey(autoGenerate = true) //idk preco toto nefunguje
//                 var userId: Long = 0,
                 val name: String,
                 val surname: String,
                 val email: String, //need to check with regex ++++ CONSTRAIN UNIQUE
                 val password: String, //simple for now, add salt and safety stuff later
                 val dateOfBirth: Date,
                 val country: String,
                 val city: String,
                 val status: String,
                 val occupation: String,
                 val annualIncome: Double,
                 val money: Double,
                 val score: Int): Serializable {
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0

//    @PrimaryKey
//    var userId: UUID = UUID.randomUUID()
}