package com.example.shayari.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shayari.R
import com.example.shayari.adapter.QuotesAdapter
import com.example.shayari.database.MyDataBaseHalper
import com.example.shayari.databinding.ActivityDisplayBinding

class DisplayActivity : AppCompatActivity() {

    lateinit var binding: ActivityDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        binding = ActivityDisplayBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        initview()
    }

    private fun initview() {

        with(binding) {
            // Pass the data of category
            val category = intent.getStringExtra("category")
            txtCategory.text = category

            // MyDatabaseHalper class object
            val dataBaseHalper = MyDataBaseHalper(this@DisplayActivity, "ShayariDb.db")

            // Fetch data from the database
            val list = dataBaseHalper.getShayariByCategory(category)

            // Import database from assets (if needed)
            dataBaseHalper.importDataBaseFromAssets()

            // QuotesAdapter class object
            val quotesAdapter =
                QuotesAdapter(this@DisplayActivity, list,
                    display_Activity = { shyari ->
                        Log.d("TAG", "initview: " + shyari)

                        var intent = Intent(this@DisplayActivity,QuotesActivity::class.java)
                        intent.putExtra("shyari",shyari)
                        startActivity(intent)
                    })

            val manager =
                LinearLayoutManager(this@DisplayActivity, LinearLayoutManager.VERTICAL, false)

            // Set up RecyclerView
            rcvDisplay.layoutManager = manager
            rcvDisplay.adapter = quotesAdapter

        }
    }
}