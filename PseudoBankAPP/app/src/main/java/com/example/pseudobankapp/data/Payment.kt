package com.example.pseudobankapp.data

import android.icu.text.SimpleDateFormat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.Locale


@Entity(tableName="Payment-table")
//@entity zakládá entitu a definuje název tabulky, ve které bude entita uložena
data class Payment(
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
    val description: String = "just Platba",
    @ColumnInfo(name = "transaction-isIncoming")
    val isIncoming: Boolean = false,
    @ColumnInfo(name = "transaction-title")
    val title: String ="Transakce",
    @ColumnInfo(name = "transaction-timestamp")
    val timestamp: String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault()).format(Date())


    )

object DummyPaymnet{
    val paymentList = listOf(
        Payment(ammount = 175.5, sender = 1425, recipientAccount = 1441, recipientName = "Karl", recipientSurname = "VonBahnhoff", description = "platba za lísky na autobus", isIncoming = false),
        Payment(ammount = 195.0, sender = 1425, recipientAccount = 1431, recipientName = "Tomáš", recipientSurname = "Buben", description = "platba za nákup"),
        Payment(ammount = 500.5, sender = 1425, recipientAccount = 1417, recipientName = "Eliáš", recipientSurname = "Želva", description = "Kredit"),
        Payment(ammount = 1500.5, sender = 1405, recipientAccount = 1425, recipientName = "Zamestnavatel", recipientSurname = "S.R.O", description = "Vyplata", isIncoming = true),
        Payment(ammount = 15.5, sender = 1425, recipientAccount = 1471, recipientName = "Kůň", recipientSurname = "Převalského", description = "platba za projížďku", isIncoming = false),
        Payment(ammount = 175.5, sender = 1425, recipientAccount = 1441, recipientName = "Karl", recipientSurname = "VonBahnhoff", description = "platba za lísky na autobus", isIncoming = false),
        Payment(ammount = 195.0, sender = 1425, recipientAccount = 1431, recipientName = "Tomáš", recipientSurname = "Buben", description = "platba za nákup"),
        Payment(ammount = 500.5, sender = 1425, recipientAccount = 1417, recipientName = "Eliáš", recipientSurname = "Želva", description = "Kredit"),
        Payment(ammount = 1500.5, sender = 1405, recipientAccount = 1425, recipientName = "Zamestnavatel", recipientSurname = "S.R.O", description = "Vyplata", isIncoming = true),Payment(ammount = 175.5, sender = 1425, recipientAccount = 1441, recipientName = "Karl", recipientSurname = "VonBahnhoff", description = "platba za lísky na autobus", isIncoming = false),
        Payment(ammount = 195.0, sender = 1425, recipientAccount = 1431, recipientName = "Tomáš", recipientSurname = "Buben", description = "platba za nákup"),
        Payment(ammount = 500.5, sender = 1425, recipientAccount = 1417, recipientName = "Eliáš", recipientSurname = "Želva", description = "Kredit"),
        Payment(ammount = 1500.5, sender = 1405, recipientAccount = 1425, recipientName = "Zamestnavatel", recipientSurname = "S.R.O", description = "Vyplata", isIncoming = true),



    )
}
