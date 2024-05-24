package sk.upjs.hackstock.entities

import androidx.room.ForeignKey

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "share", foreignKeys = [
    ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.RESTRICT //TOTO MOZNOESTE ASI ZMENIT
    )
])
data class Share (
    val userId: Long,
    val company: String,
    val shortname: String,
    val price: Double,
    val amount: Double): Serializable {

    @PrimaryKey(autoGenerate = true)
    var shareId: Long = 0

//    @PrimaryKey
//    var userId: UUID = UUID.randomUUID()
}