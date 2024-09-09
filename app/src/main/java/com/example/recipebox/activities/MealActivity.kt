package com.example.recipebox.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipebox.R
import com.example.recipebox.databinding.ActivityMealBinding
import com.example.recipebox.db.MealDatabase
import com.example.recipebox.fragments.HomeFragment
import com.example.recipebox.pojo.Meal
import com.example.recipebox.viewModel.MealViewModel
import com.example.recipebox.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealMvvm:MealViewModel
    private lateinit var youTubeLink:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_meal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding=ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase=MealDatabase.getInstance(this)
        val viewModelFactory=MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]

        getMealFromIntent()

        setInformationInViews()

        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observeMealDetailsLiveData()

        onYouTubeImgClick()

        onFavouriteClick()
    }

    private fun onFavouriteClick() {
        binding.btnAddToFav.setOnClickListener{
            mealToSave?.let{
                mealMvvm.insertMeal(it)
                Toast.makeText(this,"Meal Saved",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var mealToSave:Meal?=null
    private fun onYouTubeImgClick() {
        binding.imgYoutube.setOnClickListener{
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(youTubeLink))
            startActivity(intent)
        }
    }

    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealDeatilLiveData().observe(this,object :Observer<Meal>{
            override fun onChanged(value: Meal) {
                onResponseCase()
                val meal=value
                mealToSave=meal
                binding.tvCategory.text="Category : ${meal!!.strCategory}"
                binding.tvArea.text="Area : ${meal!!.strArea}"
                binding.tvInstructionsSteps.text=meal.strInstructions

                youTubeLink= meal.strYoutube.toString()
            }

        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        binding.collapsingToolbar.title=mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealFromIntent() {
        val intent=intent
        mealId=intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName=intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb=intent.getStringExtra(HomeFragment.MEAL_Thumb)!!
    }

    private fun loadingCase(){
        binding.progressBar.visibility=View.VISIBLE
        binding.btnAddToFav.visibility=View.INVISIBLE
        binding.tvInstructions.visibility=View.INVISIBLE
        binding.tvCategory.visibility=View.INVISIBLE
        binding.tvArea.visibility=View.INVISIBLE
        binding.imgYoutube.visibility=View.INVISIBLE
    }

    private fun onResponseCase(){
        binding.progressBar.visibility=View.INVISIBLE
        binding.btnAddToFav.visibility=View.VISIBLE
        binding.tvInstructions.visibility=View.VISIBLE
        binding.tvCategory.visibility=View.VISIBLE
        binding.tvArea.visibility=View.VISIBLE
        binding.imgYoutube.visibility=View.VISIBLE
    }
}