package com.example.recipebox.fragments



import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipebox.CategoryMealsActivity
import com.example.recipebox.activities.MainActivity
import com.example.recipebox.activities.MealActivity
import com.example.recipebox.adapters.CategoriesAdapter
import com.example.recipebox.adapters.MostPopularAdapters
import com.example.recipebox.databinding.FragmentHomeBinding
import com.example.recipebox.pojo.MealsByCategory
import com.example.recipebox.pojo.Meal
import com.example.recipebox.viewModel.HomeViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding;
    private lateinit var viewModel:HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter:MostPopularAdapters
    private lateinit var categoriesAdapter:CategoriesAdapter

    companion object{
        const val MEAL_ID="com.example.recipebox.fragments.idMeal"
        const val MEAL_NAME="com.example.recipebox.fragments.nameMeal"
        const val MEAL_Thumb="com.example.recipebox.fragments.thumbMeal"
        const val CATEGORY_NAME="com.example.recipebox.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        viewModel=(activity as MainActivity).viewModel
        viewModel=ViewModelProvider(this)[HomeViewModel::class.java]
        popularItemsAdapter=MostPopularAdapters()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()
        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomClick()

        viewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()

        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={category->
            val intent=Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter=CategoriesAdapter()
        binding.recViewCat.apply {
            layoutManager=GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter=categoriesAdapter
        }
    }


    private fun observeCategoriesLiveData() {
        viewModel.observeCateogriesLiveData().observe(viewLifecycleOwner,Observer{categories->
            categoriesAdapter.setCategoryList(categories)

        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick={meal->
            val intent=Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_Thumb,meal.strMealThumb)
            startActivity(intent)

        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPop.apply {
            layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter=popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner
        ) { mealList->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomClick() {
        binding.randomMealCard.setOnClickListener{
            val intent=Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_Thumb,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner
        ) { value ->
            Glide.with(this@HomeFragment)
                .load(value!!.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal=value
        }
    }

//    private fun observerRandomMeal() {
//        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner) { meal ->
//            meal?.let {
//                Glide.with(this@HomeFragment)
//                    .load(it.strMealThumb)
//                    .into(binding.imgRandomMeal)
//
//                // Assign the observed meal to a class property if needed
//                this.randomMeal = it
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }
}