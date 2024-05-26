package sk.upjs.hackstock.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity(tableName = "activity", foreignKeys = [
    ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.NO_ACTION
    ),
    ForeignKey(
        entity = Share::class,
        parentColumns = ["shareId"],
        childColumns = ["shareId"],
        //onDelete = ForeignKey.NO_ACTION
    )
])
data class Activity (
    val userId: Long,
    val shareId: Long,
    val date: Date,
    val sell: Boolean,
    val profit: Boolean
    ): Serializable {
    @PrimaryKey(autoGenerate = true)
    var activityId: Long = 0
}