package com.example.moneytracker

import androidx.room.*

@Dao
interface SpentDao {
    @Query("SELECT * from spents")
    //function that will execute the query
    fun getAll():List<Spent>

    @Insert
    fun insertAll (vararg spent: Spent)

    @Delete
    fun delete(spent: Spent)

    @Update
    fun update(vararg spent: Spent)
}