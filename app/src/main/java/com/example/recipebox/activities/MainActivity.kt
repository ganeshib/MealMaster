package com.example.recipebox.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.recipebox.R
import com.example.recipebox.db.MealDatabase
import com.example.recipebox.viewModel.HomeViewModel
//import com.example.recipebox.viewModel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
//    val viewModel:HomeViewModel by lazy {
//        val mealDatabase=MealDatabase.getInstance(this)
//        val homeViewModelProviderFactory=HomeViewModelFactory(mealDatabase)
//        ViewModelProvider(this,homeViewModelProviderFactory)[HomeViewModel::class.java]
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bottomNavigation= findViewById<BottomNavigationView>(R.id.btn_nav)
        val NavController=Navigation.findNavController(this, R.id.host_frag)

        NavigationUI.setupWithNavController(bottomNavigation,NavController)


    }
}