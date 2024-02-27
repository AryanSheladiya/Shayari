package com.example.shayari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shayari.activity.CategoryActivity
import com.example.shayari.adapter.CategoryAdapter
import com.example.shayari.database.MyDataBaseHalper
import com.example.shayari.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initview()
    }

    private fun initview() {

//        splash screen
        Handler().postDelayed({
            val i = Intent(this@MainActivity, CategoryActivity::class.java)
            startActivity(i)
            finish()
        }, 1000)
    }
}