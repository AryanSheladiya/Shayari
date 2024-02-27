package com.example.shayari.adapter

import Favourite
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shayari.R

class FavouriteAdapter(
    var context: Context,
    var list: ArrayList<Favourite>,
    var like: (Int, Int) -> Unit
):RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {


    class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var txtShayari : TextView = view.findViewById(R.id.txtShayari)
        var imglike : ImageView = view.findViewById(R.id.imglike)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.favourite_item_file,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        with(holder) {
            txtShayari.text = list[position].shayari

            imglike.setOnClickListener {
                like.invoke(list[position].shayariid,0)
                imglike.setImageResource(R.drawable.like)
                list[position].status = 0
                DeleteItem(position)

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun UpdateList(list: ArrayList<Favourite>){
        this.list = list
        notifyDataSetChanged()
    }

    fun DeleteItem(position: Int){
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,list.size)
    }
}