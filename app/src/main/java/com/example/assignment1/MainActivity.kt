package com.example.assignment1

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mealDescriptionEditText: EditText
    private lateinit var mealTypeSpinner: Spinner
    private lateinit var addMealButton: Button
    private lateinit var mealListView: ListView
    private lateinit var filterSpinner: Spinner
    private val mealList = mutableListOf<String>()
    private val filteredMealList = mutableListOf<String>()
    private lateinit var adapter: MealAdapter
    private lateinit var filterAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mealDescriptionEditText = findViewById(R.id.mealDescriptionEditText)
        mealTypeSpinner = findViewById(R.id.mealTypeSpinner)
        addMealButton = findViewById(R.id.addMealButton)
        mealListView = findViewById(R.id.mealListView)
        filterSpinner = findViewById(R.id.filterSpinner)

        // Set up the spinner for meal types
        ArrayAdapter.createFromResource(
            this,
            R.array.meal_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mealTypeSpinner.adapter = adapter
        }

        // Set up the spinner for filtering meals
        val filterOptions = arrayOf("All", "Breakfast", "Lunch", "Dinner")
        filterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filterOptions)
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = filterAdapter

        // Set up the ListView adapter
        adapter = MealAdapter(this, filteredMealList)
        mealListView.adapter = adapter

        // Set listeners
        addMealButton.setOnClickListener {
            addMeal()
        }

        mealTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Handle meal type selection
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle no meal type selected
            }
        }

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                filterMeals(filterOptions[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle no filter selected
            }
        }

        mealListView.setOnItemClickListener { _, _, position, _ ->
            showMealDetails(filteredMealList[position])
        }

        mealListView.setOnItemLongClickListener { _, _, position, _ ->
            removeMeal(position)
            true
        }
    }

    private fun addMeal() {
        val mealDescription = mealDescriptionEditText.text.toString()
        val mealType = mealTypeSpinner.selectedItem.toString()

        if (mealDescription.isNotEmpty()) {
            mealList.add("$mealType: $mealDescription")
            filterMeals(filterSpinner.selectedItem.toString())
            mealDescriptionEditText.text.clear()
        } else {
            Toast.makeText(this, "Please enter a meal description", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showMealDetails(meal: String) {
        val parts = meal.split(": ")
        val mealType = parts[0]
        val mealDescription = parts[1]

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(mealType)
        dialogBuilder.setMessage("Description: $mealDescription")
        dialogBuilder.setPositiveButton("OK", null)
        dialogBuilder.show()
    }

    private fun removeMeal(position: Int) {
        val actualPosition = mealList.indexOf(filteredMealList[position])
        AlertDialog.Builder(this)
            .setTitle("Remove Meal")
            .setMessage("Are you sure you want to remove this meal?")
            .setPositiveButton("Yes") { _, _ ->
                mealList.removeAt(actualPosition)
                filterMeals(filterSpinner.selectedItem.toString())
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun filterMeals(filter: String) {
        filteredMealList.clear()
        if (filter == "All") {
            filteredMealList.addAll(mealList)
        } else {
            filteredMealList.addAll(mealList.filter { it.startsWith(filter) })
        }
        adapter.notifyDataSetChanged()
    }
}
