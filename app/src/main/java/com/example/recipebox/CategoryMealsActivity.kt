package com.example.recipebox

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipebox.adapters.CategoryMealsAdapter
import com.example.recipebox.databinding.ActivityCategoryMealsBinding
import com.example.recipebox.fragments.HomeFragment
import com.example.recipebox.viewModel.CategoryMealViewModel

class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealViewModel:CategoryMealViewModel
    lateinit var categoryMealsAdapter:CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_meals)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding=ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        prepareRecyclerView()

        categoryMealViewModel=ViewModelProvider(this)[CategoryMealViewModel::class.java]

        categoryMealViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealViewModel.observeMealsLiveData().observe(this,Observer{mealsList->
            binding.tvCategoryCount.text=mealsList.size.toString()
            categoryMealsAdapter.setMealsList(mealsList)
        })
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter=CategoryMealsAdapter()
        binding.rxMeals.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter=categoryMealsAdapter
        }
    }
}