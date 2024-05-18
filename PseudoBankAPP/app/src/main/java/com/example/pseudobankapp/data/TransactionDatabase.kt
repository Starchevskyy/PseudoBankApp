package com.example.pseudobankapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(
    //možná je problém v Person Class => pokud se databáze nerozjede, je potřeba zkusit to zakomentovat
    // entities = [Transaction::class],
    //version = 1,
    //exportSchema = false

    entities = [Transaction::class, Person::class],
    version = 1,
    exportSchema = false
)
abstract class TransactionDatabase : RoomDatabase() {
    // payment dao se již nebude využívat
    //abstract fun paymentDao():PaymentDao
    abstract fun transactionDao():TransactionDao
    //
    //funkce transactionDao() bude přistupovat do databáze skrze metody nadefinované v souboru TransactionDao


}
