package com.example.moneytracker

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Spent::class), version = 1)
abstract class Database : RoomDatabase()
{
    abstract fun spentDao(): SpentDao
}