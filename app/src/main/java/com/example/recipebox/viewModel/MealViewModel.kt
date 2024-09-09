package com.example.recipebox.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebox.db.MealDatabase
import com.example.recipebox.pojo.Meal
import com.example.recipebox.pojo.MealList
import com.example.recipebox.retrofit.RetroFitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    val mealDatabase:MealDatabase
):ViewModel() {
    private var mealDetailLiveData=MutableLiveData<Meal>()

    fun getMealDetail(id:String){
        RetroFitInstance.api.getMealDetails(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body()!=null){
                    mealDetailLiveData.value=response.body()!!.meals[0]
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity",t.message.toString())
            }
        })
    }

    fun observeMealDeatilLiveData():LiveData<Meal>{
        return mealDetailLiveData;
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }
}