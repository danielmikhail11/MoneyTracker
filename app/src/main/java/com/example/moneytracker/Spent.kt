package com.example.moneytracker

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.temporal.TemporalAmount

@Entity(tableName = "spents")
data class Spent(@PrimaryKey(autoGenerate = true) val id: Int,
                 val label:String, val amount: Double,
                 val notes: String): Serializable
{

}