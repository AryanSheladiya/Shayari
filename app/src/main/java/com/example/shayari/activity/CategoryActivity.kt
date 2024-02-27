package com.example.shayari.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shayari.R
import com.example.shayari.adapter.CategoryAdapter
import com.example.shayari.database.MyDataBaseHalper
import com.example.shayari.databinding.ActivityCategoryBinding


class CategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        binding = ActivityCategoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initview()
    }

    private fun initview() {
        with(binding) {
//            MyDataBaseHalper class object
            var dataBaseHalper = MyDataBaseHalper(this@CategoryActivity, "ShayariDb.db")
            dataBaseHalper.importDataBaseFromAssets()

//            list
            var list = dataBaseHalper.readCategory()

//            Adapter class object
            var categoryAdapter = CategoryAdapter(list, crdcategory = { category ->

                Log.d("TAG", "initview: " + category)

//                intent
                var intent = Intent(this@CategoryActivity, DisplayActivity::class.java)
                intent.putExtra("category", category)
                startActivity(intent)
            })

            val manager =
                GridLayoutManager(this@CategoryActivity,2, GridLayoutManager.VERTICAL,false)

//            Set up RecyclerView
            rcvCategory.layoutManager = manager
            rcvCategory.adapter = categoryAdapter

//            Like Quotes
            imgLikeQuotes.setOnClickListener {
                var intent = Intent(this@CategoryActivity,FavouriteActivity::class.java)
                startActivity(intent)
            }
        }
    }
}