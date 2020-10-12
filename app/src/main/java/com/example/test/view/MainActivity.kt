package com.example.test.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.test.APP_ACTIVITY
import com.example.test.viewModel.MainViewModel
import com.example.test.R
import com.example.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        navController = Navigation.findNavController(this,
            R.id.nav_host_fragment
        )
        APP_ACTIVITY = this
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}