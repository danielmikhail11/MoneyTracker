package com.example.moneytracker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var Spents :List<Spent>
    private lateinit var beforeSpent :List<Spent>
    private lateinit var spentAdapter: SpentAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var database: Database
    private lateinit var deletedSpent: Spent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Spents = arrayListOf()
        spentAdapter = SpentAdapter(Spents)
        linearLayoutManager = LinearLayoutManager(this)

        database = Room.databaseBuilder(this,Database::class.java,"spents").build()

        recyclerview.apply {
            adapter = spentAdapter
            layoutManager = linearLayoutManager
        }

        //swipe to delete the selected one to the right
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
        {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteSpent(Spents[viewHolder.adapterPosition])
            }
        }
        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recyclerview)


        addBtn.setOnClickListener {
            val intent = Intent(this, AddButton:: class.java)
            startActivity(intent)
        }
    }

    private fun callAll()
    {
        //code inside will be called in background thread
        GlobalScope.launch {

            Spents = database.spentDao().getAll()

            runOnUiThread {
                updateDashboard()
                spentAdapter.setAdapterData(Spents)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateDashboard()

    {
        // goes over it and only takes the amount and with the new list for the spent
        val totalAmount = Spents.map { it.amount }.sum()
        //list of all the amount that are greater than 0
        val incomeAmount =Spents.filter { it.amount>0 }.map { it.amount }.sum()
        val expenseAmount = totalAmount - incomeAmount

        balance.text= "RM %.2f".format(totalAmount)
        income.text= "RM %.2f".format(incomeAmount)
        expense.text= "RM %.2f".format(expenseAmount)
    }


    private fun undoDelete() {
        GlobalScope.launch {
            database.spentDao().insertAll(deletedSpent)

            Spents = beforeSpent

            runOnUiThread {
                updateDashboard()
                spentAdapter.setAdapterData(Spents)
            }
        }
    }


    private fun showNotificationbar(){
        val view = findViewById<View>(R.id.coordinator)
        val notificationbar = Snackbar.make(view, "The selected has been deleted!", Snackbar.LENGTH_LONG)
        notificationbar.setAction("Undo"){
            undoDelete()
        }

            .setActionTextColor(ContextCompat.getColor(this,R.color.teal_200))
            .setTextColor(ContextCompat.getColor(this,R.color.white))
            .show()
    }

    private fun deleteSpent(spent: Spent){
        deletedSpent = spent
        beforeSpent = Spents

        GlobalScope.launch {
            database.spentDao().delete(spent)

            Spents = Spents.filter { it.id != spent.id }
            runOnUiThread {
                updateDashboard()
                spentAdapter.setAdapterData(Spents)
                showNotificationbar()
            }
        }
    }


    //update screen when it called
    override fun onResume() {
        super.onResume()
        callAll()
    }
}