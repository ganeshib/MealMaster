package com.example.recipebox.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipebox.db.MealDatabase
import com.example.recipebox.pojo.Category
import com.example.recipebox.pojo.CategoryList
import com.example.recipebox.pojo.MealsByCategoryList
import com.example.recipebox.pojo.MealsByCategory

import com.example.recipebox.pojo.Meal
import com.example.recipebox.pojo.MealList
import com.example.recipebox.retrofit.RetroFitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
//    private val mealDatabase: MealDatabase
):ViewModel() {
    private var popularItemsLiveData=MutableLiveData<List<MealsByCategory>>()
    private var randomMealLiveData= MutableLiveData<Meal>()
    private var categoriesLiveData=MutableLiveData<List<Category>>()
//    private var favouriteMealsLiveData=mealDatabase.mealDao().getAllmeals()

    fun getRandomMeal(){
        RetroFitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal: Meal =response.body()!!.meals[0]
                    Log.d("TEST","meal id ${randomMeal.idMeal} name ${randomMeal.strMeal}")
                    randomMealLiveData.value=randomMeal
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }
        })
    }

    fun getPopularItems(){
        RetroFitInstance.api.getPopularItems("Seafood").enqueue(object:Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body()!=null){
                    popularItemsLiveData.value=response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }

    fun getCategories(){
        RetroFitInstance.api.getCategories().enqueue(object :Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeViewModel",t.message.toString())
            }

        })
    }

    fun observeRandomMealLiveData():LiveData<Meal>{
        return randomMealLiveData
    }

    fun observePopularItemsLiveData():LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }

    fun observeCateogriesLiveData():LiveData<List<Category>>{
        return categoriesLiveData
    }

//    fun observeFavouriteMealsLiveData():LiveData<List<Meal>>{
//        return favouriteMealsLiveData
//    }
}