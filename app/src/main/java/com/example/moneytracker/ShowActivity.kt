package com.example.moneytracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.provider.SyncStateContract.Helpers.update
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_add_button.*
import kotlinx.android.synthetic.main.activity_add_button.amountInput
import kotlinx.android.synthetic.main.activity_add_button.amountLayout
import kotlinx.android.synthetic.main.activity_add_button.categoriesInput
import kotlinx.android.synthetic.main.activity_add_button.categoriesLayout
import kotlinx.android.synthetic.main.activity_add_button.exitBtn
import kotlinx.android.synthetic.main.activity_add_button.notesInput
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShowActivity : AppCompatActivity() {
    private lateinit var spent: Spent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        var spent= intent.getSerializableExtra("spents") as Spent

        categoriesInput.setText(spent.label)
        amountInput.setText(spent.amount.toString())
        notesInput.setText(spent.notes)


        show_view.setOnClickListener{
            this.window.decorView.clearFocus()

            //to hide keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            //deals with the keyboard
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }


        categoriesInput.addTextChangedListener{
            editBtn.visibility = View.VISIBLE
            //!! = not null
            if (it!!.count() > 0)
                categoriesLayout.error = null
        }
        // berlaku pas klo uda diisi jadi ga muncul error
        amountInput.addTextChangedListener{
            editBtn.visibility = View.VISIBLE
            //!! = not null
            if (it!!.count() > 0)
                amountLayout.error = null
        }
        notesInput.addTextChangedListener {
            editBtn.visibility = View.VISIBLE
            //!! = not null
        }

        editBtn.setOnClickListener{
            val categories = categoriesInput.text.toString()
            val notes = notesInput.text.toString()
            //toDoubleOrNull() = if the field is empty there will be no value
            val amount = amountInput.text.toString().toDoubleOrNull()

            if (categories.isEmpty())
                categoriesLayout.error = "Please enter a category"

            else if (amount == null)
                amountLayout.error = "Please enter the amount"

            else{
                val spent = Spent(spent.id,categories, amount, notes)
                update(spent)
            }
        }

        exitBtn.setOnClickListener {
            finish()
        }
    }

    private fun update(spent: Spent){
        val database = Room.databaseBuilder(this,Database::class.java,"spents").build()

        GlobalScope.launch {
            database.spentDao().update(spent)
            finish()
        }
    }
}