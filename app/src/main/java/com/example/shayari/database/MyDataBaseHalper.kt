package com.example.shayari.database

import Category
import Favourite
import Shayari
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class MyDataBaseHalper(var context: Context, var name: String) :
    SQLiteOpenHelper(context, name, null, 1) {

    var DB_PATH: String = ""
    var divider = "/"
    var DB_NAME: String = "ShayariDb.db"

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun isDataBaseExists(): Boolean {
        DB_PATH =
            divider + "data" + divider + "data" + divider + context.getPackageName() + divider + "databases/";
        val dbFile: File = File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    @Throws(IOException::class)
    fun importDataBaseFromAssets() {
        if (!isDataBaseExists()) {
            this.readableDatabase
            val myInput: InputStream = context.assets.open(DB_NAME)
            val outFileName: String = DB_PATH + DB_NAME
            val myOutput: OutputStream = FileOutputStream(outFileName)
            val buffer = ByteArray(1024)
            var length: Int
            while (myInput.read(buffer).also { length = it } > 0) {
                myOutput.write(buffer, 0, length)
            }

//            Close the streams
            myOutput.flush()
            myOutput.close()
            myInput.close()
        }
    }

//    CategoryTB
    fun readCategory(): ArrayList<Category> {
        var list: ArrayList<Category> = ArrayList()
        var db = readableDatabase

        var sql = "select * from categoryTB "
        var cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            do {

                var categoryid = cursor.getInt(0)
                var category = cursor.getString(1)
                var image = cursor.getString(2)

                var model = Category(categoryid, category,image)

                list.add(model)
                Log.d("TAG", "readCategory: " + category+image)
            } while (cursor.moveToNext())
        }
        return list
    }

//    ShyariTB
    fun getShayariByCategory(category: String?): ArrayList<Shayari> {
        val list: ArrayList<Shayari> = ArrayList()
        val db = readableDatabase
        val sql = "SELECT * FROM ShayariTB WHERE categoryid IN (SELECT categoryid FROM categoryTB WHERE category = ?)"
        val cursor = db.rawQuery(sql, arrayOf(category))
        if (cursor.moveToFirst()) {
            do {
                val shayariid = cursor.getInt(0)
                val shayari = cursor.getString(1)
                val categoryid = cursor.getInt(2)
                val status = cursor.getInt(3)
                val model = Shayari(shayariid, shayari, categoryid,status)
                list.add(model)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

//    like
    fun updateLikeStatus(shayariid: Int, status: Int) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("Status", status) // assuming "Status" is the column name for the like status

        db.update("ShayariTB", values, "shayariid=?", arrayOf(shayariid.toString()))
        db.close()
    }

    fun FavoriteDisplayRecord() : ArrayList<Favourite>{
        var DisplayList = ArrayList<Favourite>()

        val dbF = readableDatabase
        val SqlF = "Select * from ShayariTB where status = 1"
        val c = dbF.rawQuery(SqlF,null)
        if (  c.moveToFirst()){

            do {
                var Shayari_id = c.getInt(0)
                var Shayari = c.getString(1)
                var fav = c.getInt(2)

                Log.e("TAG", "FavoriteDisplayRecord: $Shayari_id $Shayari" )
                var shayarimodal = Favourite(Shayari_id,Shayari,fav)

                DisplayList.add(shayarimodal)
            }while (c.moveToNext())
        }
        return DisplayList
    }

    fun UpdeteRecord(id: Int, status: Int) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("status", status)

        val whereClause = "shayariid = ?"
        val whereArgs = arrayOf(id.toString())

        db.update("ShayariTB", contentValues, whereClause, whereArgs)
        db.close()
    }
}