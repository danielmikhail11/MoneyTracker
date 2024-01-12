package com.example.moneytracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_add_button.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddButton : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_button)

        categoriesInput.addTextChangedListener{
            //!! = not null
            if (it!!.count() > 0)
                categoriesLayout.error = null
        }

        //occurs if it has been filled so there will be no error
        amountInput.addTextChangedListener{
            //!! = not null
            if (it!!.count() > 0)
                amountLayout.error = null
        }

        confirmBtn.setOnClickListener{
            val categories = categoriesInput.text.toString()
            val notes = notesInput.text.toString()
            //toDoubleOrNull() = if the field is empty there will be no value
            val amount = amountInput.text.toString().toDoubleOrNull()


            if (categories.isEmpty())
                categoriesLayout.error = "Please enter a category"

            else if (amount == null)
                amountLayout.error = "Please enter the amount"

            else{
                val spent = Spent(0,categories, amount, notes)
                insert(spent)
            }
        }

        exitBtn.setOnClickListener {
            finish()
        }
    }

    private fun insert(spent: Spent){
        val database = Room.databaseBuilder(this,Database::class.java,"spents").build()

        GlobalScope.launch {
            database.spentDao().insertAll(spent)
            finish()
        }
    }
}