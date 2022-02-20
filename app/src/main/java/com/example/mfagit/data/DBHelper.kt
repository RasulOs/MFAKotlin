package com.example.mfagit.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import com.example.mfagit.MenuCategoriesFragment
import com.example.mfagit.PinCodeFragment

class DBHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        val DB_NAME = "Login.db"
        val DB_VERSION = 1
    }


    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        sqLiteDatabase?.execSQL("CREATE TABLE users(_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, " +
                "email TEXT, mobile TEXT, pin TEXT, password TEXT)")
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, p1: Int, p2: Int) {
        sqLiteDatabase?.execSQL("DROP TABLE IF EXISTS users")
    }

    fun insertData(username: String, email: String, mobile: String, pin: String, password: String): Boolean {

        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("email", email)
        contentValues.put("mobile", mobile)
        contentValues.put("pin", pin)
        contentValues.put("password", password)

        val result = myDB.insert("users", null, contentValues)

        return result != -1L
    }

    fun checkUsername(email: String): Boolean {
        val myDB = this.writableDatabase

        val cursor = myDB.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))

        return cursor.count > 0
    }

    fun checkUsernameAndPassword(email: String, password: String): Boolean {
        val myDB = this.writableDatabase

        val cursor = myDB.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?",
        arrayOf(email, password))

        return cursor.count > 0
    }

    fun initializePIN(email: String) {
        val myDB = this.writableDatabase
        val cursor = myDB.rawQuery("SELECT pin FROM users WHERE email = ?", arrayOf(email))
        while (cursor.moveToNext()) {
            PinCodeFragment.setPinFromDB(cursor.getString(0))
        }
    }

    fun initializeName(email: String) {
        val myDB = this.writableDatabase
        val cursor =
            myDB.rawQuery("SELECT username FROM users WHERE email = ?", arrayOf(email))

        while (cursor.moveToNext()) {
            MenuCategoriesFragment.setName(cursor.getString(0))
        }
    }


}