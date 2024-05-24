package sk.upjs.hackstock.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "question")
data class Question (
    val text: String,
    val option1: String,
    val option2: String,
    val correctOption: String
):Serializable{
    @PrimaryKey(autoGenerate = true)
    var questionId: Long = 0
}