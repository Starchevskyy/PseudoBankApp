package com.example.pseudobankapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.pseudobankapp.data.TransactionDatabase
import com.example.pseudobankapp.data.TransactionRepository

object Graph {
    //v kotlinu object znamená singleton => lze jej založit pouze jednou, což je fajn pro databázi
    lateinit var database: TransactionDatabase
    val transactionRepository by lazy {
        //by lazy zajistuje ze je vsechno v by lazy inicializovano pouze pokud je to vyzadovano
        //což umoznuje setrit prostredky
        TransactionRepository(transactionDao = database.transactionDao())
    }
    fun provide(context: Context){
        database = Room.databaseBuilder(context,TransactionDatabase::class.java,"transactions.db").build()
        //definuce názvu databáze v telefonu
        // zbuildění databáze
    }
}