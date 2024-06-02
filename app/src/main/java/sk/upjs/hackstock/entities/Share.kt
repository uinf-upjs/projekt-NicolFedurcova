package sk.upjs.hackstock.entities

import android.os.Parcel
import android.os.Parcelable
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
        onDelete = ForeignKey.NO_ACTION //TOTO MOZNOESTE ASI ZMENIT
    )
])
data class Share (
    val userId: Long,
    val company: String,
    val shortname: String,
    val price: Double,
    val amount: Double,
    val visibility: Byte): Serializable, Parcelable {

    @PrimaryKey(autoGenerate = true)
    var shareId: Long = 0

//    @PrimaryKey
//    var userId: UUID = UUID.randomUUID()

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readByte()

    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(userId)
        parcel.writeString(company)
        parcel.writeString(shortname)
        parcel.writeDouble(price)
        parcel.writeDouble(amount)
        parcel.writeByte(visibility)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Share> {
        override fun createFromParcel(parcel: Parcel): Share {
            return Share(parcel)
        }

        override fun newArray(size: Int): Array<Share?> {
            return arrayOfNulls(size)
        }
    }
}