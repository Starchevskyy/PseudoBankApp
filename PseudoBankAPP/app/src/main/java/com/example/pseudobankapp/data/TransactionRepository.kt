package com.example.pseudobankapp.data

import kotlinx.coroutines.flow.Flow


// tyto funkce budou použity ve view model
class TransactionRepository(private val transactionDao: TransactionDao) {
    suspend fun addTransaction(transaction: Transaction){
        transactionDao.addTransaction(transaction)
    }
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()
    //pokud nebude fungovta, ověřit tuto funkci v transactionRepository
    fun getATransactionById(id:Long): Flow<Transaction>{
        //funkce vrátí následující
        return transactionDao.getATransactionById(id)

    }
    suspend fun updateATransaction(transaction: Transaction){
         transactionDao.updateATransaction(transaction)
    }
    suspend fun  deleteATransaction(transaction: Transaction){
        transactionDao.deleteATransaction(transaction)
    }
    //----------------------
    // metody pro uzivatele
    suspend fun addPerson(person: Person) {
        transactionDao.addPerson(person)
    }

    fun getAllPersons(): Flow<List<Person>> = transactionDao.getAllPersons()

    fun getPersonById(acctId: Long): Flow<Person> {
        return transactionDao.getPersonById(acctId)
    }

    suspend fun updatePerson(person: Person) {
        transactionDao.updatePerson(person)
    }

    suspend fun deletePerson(person: Person) {
        transactionDao.deletePerson(person)
    }

    fun getPersonByLogin(login: String): Flow<Person> {
        return transactionDao.getPersonByLogin(login)
    }
    //
    suspend fun executeTransaction(transaction: Transaction) {
        // Přidání záznamu o transakci (pokud je potřeba)
        //transactionDao.addTransaction(transaction)

        // Aktualizace zůstatku odesílatele
        transactionDao.updateBalance(transaction.sender.toLong(), -transaction.ammount)

        // Aktualizace zůstatku příjemce
        transactionDao.updateBalance(transaction.recipientAccount.toLong(), transaction.ammount)
    }
    suspend fun getBalance(accountId: Long): Double {
        return transactionDao.getBalance(accountId)
    }
    fun getTransactionsByUser(userId: Int): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByUser(userId)
    }


}
