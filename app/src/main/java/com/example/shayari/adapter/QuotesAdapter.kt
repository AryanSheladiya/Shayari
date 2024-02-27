package com.example.shayari.adapter

import Shayari
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shayari.R
import com.example.shayari.database.MyDataBaseHalper

class QuotesAdapter(
    var context: Context,
    var list: ArrayList<Shayari>,
    var display_Activity: ((shyari: String) -> Unit)
) :
    RecyclerView.Adapter<QuotesAdapter.MyViewHolder>() {

    private val sharedPreferences =
        context.getSharedPreferences("LikedStatus", Context.MODE_PRIVATE)

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtShayari: TextView = view.findViewById(R.id.txtShayari)
        var imgCopy: ImageView = view.findViewById(R.id.imgCopy)
        var imgShare: ImageView = view.findViewById(R.id.imgShare)
        var imglike: ImageView = view.findViewById(R.id.imglike)
        var imgEdit: ImageView = view.findViewById(R.id.imgEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.quotes_item_file, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuotesAdapter.MyViewHolder, position: Int) {
        with(holder) {

//            text Shayari
            var shayariText = list[position].shayari
            txtShayari.text = shayariText

//            Shayari Copy
            imgCopy.setOnClickListener {
                // Call the copyToClipboard function with the Shayari text
                copyToClipboard(it.context, shayariText)
            }

//            Share Shayari
            imgShare.setOnClickListener {
                // Call the shareQuote function with the Shayari text
                shareQuote(it.context, shayariText)
            }

//            like Shayari
            imglike.setOnClickListener {
                // Toggle the like status
                list[position].status = if (list[position].status == 1) {
                    0
                } else {
                    1
                }

                // Update the UI
                val updatedLikeStatusResource =
                    if (list[position].status == 1) {
                        R.drawable.likefild
                    } else {
                        R.drawable.like
                    }
                imglike.setImageResource(updatedLikeStatusResource)

                // Add logs
                val likeStatus = if (list[position].status == 1) {
                    "Liked"
                } else {
                    "Unliked"
                }
                Log.d(
                    "LikeStatus",
                    "$likeStatus - Position: $position, ShayariID: ${list[position].shayariid}"
                )

                // Update the like status in SharedPreferences
                sharedPreferences.edit()
                    .putInt("liked_${list[position].shayariid}", list[position].status).apply()

                // Update the like status in the database
                updateLikeStatusInDatabase(list[position].shayariid, list[position].status)
            }

//            Like to Unlike
            val storedLikeStatus = sharedPreferences.getInt("liked_${list[position].shayariid}", 0)
            list[position].status = storedLikeStatus
            val currentLikeStatusResource =
                if (storedLikeStatus == 1) {
                    R.drawable.likefild
                } else {
                    R.drawable.like
                }
            imglike.setImageResource(currentLikeStatusResource)

//            Click to Display Activity
            imgEdit.setOnClickListener {
                display_Activity.invoke(list[position].shayari)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    //    like Shayari to set image resource
    private fun updateLikeStatusInDatabase(shayariID: Int, status: Int) {
        val dbHelper = MyDataBaseHalper(context, "ShayariDb.db")
        dbHelper.updateLikeStatus(shayariID, status)
    }

    //    copy Shayari
    private fun copyToClipboard(context: Context, text: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Quote", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "Quote copied to clipboard !", Toast.LENGTH_SHORT).show()
    }

    //    share shayari
    private fun shareQuote(context: Context, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(shareIntent, "Share using"))
    }
}