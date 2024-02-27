package com.example.shayari.adapter

import Category
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.shayari.R

class CategoryAdapter(
    var list: ArrayList<Category>,
    var crdcategory: ((category: String) -> Unit)
) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtCategory: TextView = view.findViewById(R.id.txtCategory)
        var crdCategory: CardView = view.findViewById(R.id.crdCategory)
        var imgCategory: ImageView = view.findViewById(R.id.imgCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item_file, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
//            text Category
            txtCategory.setText(list[position].category)

//            invoke
            crdCategory.setOnClickListener {
                crdcategory.invoke(list[position].category)
            }

//            Image from external database
            val byteArray = Base64.decode(list[position].image, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            imgCategory.setImageBitmap(bitmap)
        }
    }
}