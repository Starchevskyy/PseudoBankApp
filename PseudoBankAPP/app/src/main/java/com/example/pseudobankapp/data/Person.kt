package com.example.pseudobankapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="person-table")
data class Person(
    @PrimaryKey(autoGenerate = true)
    val acctId:Long = 0L,
    @ColumnInfo(name="person-name")
    val name:String="",
    @ColumnInfo(name="person-second-name")
    val secondName: String = "",
    @ColumnInfo(name="person-login")
    val login : String = "",
    @ColumnInfo(name="person-password")
    val passwd : String = "",
    @ColumnInfo(name="person-balance")
    val balance: Double = 0.0
)
