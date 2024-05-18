package com.example.pseudobankapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
abstract class TransactionDao {
    //DAO = Data Abstraction Object
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addTransaction(transaction: Transaction)

    @Query("SELECT * FROM `transaction-table` ORDER BY id DESC")
    abstract fun getAllTransactions(): Flow<List<Transaction>>
    //flow vrátí seznam (list) transakcí
    @Update
    abstract suspend fun updateATransaction(transactionEntity: Transaction)

    @Delete
    abstract suspend fun deleteATransaction(transactionEntity: Transaction)
    @Query("SELECT * FROM `transaction-table` WHERE id=:id")
    abstract fun getATransactionById(id:Long): Flow<Transaction>
    // flow vrátí pouze jednu transakci
    // Metody pro osoby
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addPerson(person: Person)

    @Query("SELECT * FROM `person-table`")
    abstract fun getAllPersons(): Flow<List<Person>>

    @Update
    abstract suspend fun updatePerson(person: Person)

    @Delete
    abstract suspend fun deletePerson(person: Person)

    @Query("SELECT * FROM `person-table` WHERE acctId=:acctId")
     abstract fun getPersonById(acctId: Long): Flow<Person>
    //  přidaná metoda pro vyhledání osoby podle loginu
    @Query("SELECT * FROM `person-table` WHERE `person-login` = :login")
    abstract fun getPersonByLogin(login: String): Flow<Person>
    //transakční querina
    @Query("UPDATE `person-table` SET `person-balance` = `person-balance` + :amount WHERE acctId = :accountId")
    abstract suspend fun updateBalance(accountId: Long, amount: Double)
    @Query("SELECT `person-balance` FROM `person-table` WHERE acctId = :accountId")
   abstract  suspend fun getBalance(accountId: Long): Double
    @Query("SELECT * FROM `transaction-table` WHERE `transaction-sender-account` =  :userId OR `transaction-recipient-account` = :userId ORDER BY id DESC")
    abstract fun getTransactionsByUser(userId: Int): Flow<List<Transaction>>



}