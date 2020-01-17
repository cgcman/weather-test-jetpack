package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.test.viewmodels.MainViewModel
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.title.observe(this, Observer {
            supportActionBar?.title = it
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}