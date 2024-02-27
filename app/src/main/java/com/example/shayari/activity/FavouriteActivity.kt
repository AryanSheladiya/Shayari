package com.example.shayari.activity

import Favourite
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shayari.R
import com.example.shayari.adapter.FavouriteAdapter
import com.example.shayari.database.MyDataBaseHalper
import com.example.shayari.databinding.ActivityFavouriteBinding

class FavouriteActivity : AppCompatActivity() {

    lateinit var binding: ActivityFavouriteBinding

    var list = ArrayList<Favourite>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        initview()
    }

    private fun initview() {
        with(binding) {

            val db = MyDataBaseHalper(this@FavouriteActivity,"ShayariDb.db")
            list = db.FavoriteDisplayRecord()

            var favorite =
                FavouriteAdapter(this@FavouriteActivity,list) { shyariid, status ->
                    db.UpdeteRecord(shyariid, status) }

            var manager = LinearLayoutManager(this@FavouriteActivity,LinearLayoutManager.VERTICAL,false)
            rcvLikeQuotes.layoutManager = manager
            rcvLikeQuotes.adapter = favorite

            favorite.UpdateList(list)
        }
    }
}