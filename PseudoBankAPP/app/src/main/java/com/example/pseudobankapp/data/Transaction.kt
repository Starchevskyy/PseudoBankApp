package com.example.pseudobankapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName="transaction-table")
//@entity zakládá entitu a definuje název tabulky, ve které bude entita uložena
data class Transaction(
    //definice primárního klíče autogenerate automaticky inkrementuje položky v databázi.
    @PrimaryKey(autoGenerate = true)
    val id : Long =0L,
    @ColumnInfo(name="transaction-ammount")
    val ammount : Double = 11.00,
    @ColumnInfo(name="transaction-sender-account")
    val sender : Int = 1,
    @ColumnInfo(name="transaction-sender-surname")
    val senderSurname : String = "Novak",
    @ColumnInfo(name="transaction-sender-name")
    val senderName : String = "Jan",
    @ColumnInfo(name="transaction-recipient-account")
    val recipientAccount : Int = 7,
    @ColumnInfo(name="transaction-recipient-name")
    val recipientName : String = "Pepa",
    @ColumnInfo(name="transaction-recipient-surname")
    val recipientSurname : String = "Stary",
    @ColumnInfo(name="transaction-desc")
    val description: String = "just Platba",//zprava pro odesilatele
    @ColumnInfo(name = "transaction-isIncoming")
    val isIncoming: Boolean = false,
    @ColumnInfo(name = "transaction-title")
    val title: String ="Transakce",//zprava pro prijemce
    @ColumnInfo(name ="transaction-timestamp")
    val timeStamp: String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault()).format(Date())
    //rozsireni tabulky transaction-table o timestamp


)