package com.example.assignment1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ArrayAdapter

class MealAdapter(context: Context, private val meals: List<String>) :
    ArrayAdapter<String>(context, 0, meals) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val meal = meals[position]
        val parts = meal.split(": ")
        val mealType = parts[0]
        val mealDescription = parts[1]

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_meal, parent, false)

        val mealIconImageView: ImageView = view.findViewById(R.id.mealIconImageView)
        val mealDescriptionTextView: TextView = view.findViewById(R.id.mealDescriptionTextView)

        mealDescriptionTextView.text = mealDescription

        val iconResource = when (mealType) {
            "Breakfast" -> R.drawable.breakfast_icon
            "Lunch" -> R.drawable.lunch_icon
            "Dinner" -> R.drawable.dinner_icon
            else -> R.drawable.default_icon // Use default_icon when mealType is unknown
        }

        mealIconImageView.setImageResource(iconResource)

        return view
    }
}
