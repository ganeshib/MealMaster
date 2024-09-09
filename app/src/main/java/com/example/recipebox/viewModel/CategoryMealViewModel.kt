package com.example.recipebox.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipebox.pojo.MealList
import com.example.recipebox.pojo.MealsByCategory
import com.example.recipebox.pojo.MealsByCategoryList
import com.example.recipebox.retrofit.RetroFitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealViewModel:ViewModel() {
    val mealsLiveData=MutableLiveData<List<MealsByCategory>>()
    fun getMealsByCategory(categoryName:String){
        RetroFitInstance.api.getMealsByCategory(categoryName).enqueue(object :Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let {
                    mealsLiveData.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("CategoryMealsViewModel",t.message.toString())
            }

        })
    }

    fun observeMealsLiveData():LiveData<List<MealsByCategory>>{
        return mealsLiveData
    }
}