package com.example.pseudobankapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pseudobankapp.Graph
import com.example.pseudobankapp.data.Payment
import com.example.pseudobankapp.data.Person
import com.example.pseudobankapp.data.Transaction
import com.example.pseudobankapp.data.TransactionRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess


class PaymentViewModel(
    private var  transactionRepository:TransactionRepository = Graph.transactionRepository
): ViewModel() {
    var isLoggedIn = mutableStateOf(false)
        private set

    //lateinit umožňuje inicializovat proměnnou až později po spuštění projektu (před ejím využitím)
    lateinit var getAllTransactions: Flow<List<Transaction>>
    lateinit var getAllTransactionsByUserId: Flow<List<Transaction>>
    var transactionTitleState by mutableStateOf("")
    var transactionDescriptionChanged by mutableStateOf("")
    var transactionAmount by mutableStateOf(0.0)
    var transactionSender by mutableStateOf(0) // ID odesílatele
    var transactionSenderName by mutableStateOf("")
    var transactionSenderSurname by mutableStateOf("")
    var transactionRecipientAccount by mutableStateOf(0) // ID účtu příjemce
    var transactionRecipientName by mutableStateOf("")
    var transactionRecipientSurname by mutableStateOf("")
    var transactionIsIncoming by mutableStateOf(false)
    var userAcctState by mutableStateOf(0.0)
    var userAcctNum by mutableStateOf(0)
    var transactionRecipientAccountString by mutableStateOf("")
    //----------------
    var loggedInUser by mutableStateOf<Person?>(null)
        private set

    //----------------
    fun onTransactionDescriptionChanged(newString: String){
        transactionDescriptionChanged = newString
    }
    fun onTransactionTitleChanged(newString: String){
        transactionTitleState = newString
    }

    init {
        //inicializace
        viewModelScope.launch {
            //getAllTransactions = transactionRepository.getAllTransactions()
            getAllTransactionsByUserId = transactionRepository.getTransactionsByUser(userAcctNum)

            //volani vzorovych dat
            //todo vyvolani vzorovych dat -> jednou stačí
            //addSampleData()
            // TODO zavolanim teto funkce se vytvori v inicializovane databazi uzivatele.
            //todo po zavolani je potreba opet zakomentovat
            //addSampleUsers()
        }
    }
    //todo tato funkce se volá při prvním spuštění aplikace
    private fun addSampleUsers() {
        val users = listOf(
            Person(name="Karl", secondName="Von Bahnhoff", login="test1", passwd="heslo", balance=101000.0),
            Person(name="Jiří", secondName="Bukovský", login="test2", passwd="heslo", balance=150100.0),
            Person(name="Roman", secondName="Černý", login="test3", passwd="heslo", balance=201000.0),
            Person(name="Andrij", secondName="Starčevskyj", login="test", passwd="heslo", balance=1201000.0)
        )
        viewModelScope.launch{
            users.forEach{user ->
            transactionRepository.addPerson(user)}
        }
    }

    private fun addSampleData() {
        // Tři vzorové transakce
        val transactions = listOf(
            Transaction(
                ammount = 1500.00,
                sender = 1,
                senderSurname = "Novak",
                senderName = "Jan",
                recipientAccount = 2,
                recipientName = "Petr",
                recipientSurname = "Horak",
                description = "Platba za služby",
                isIncoming = false,
                title = "Platba"
            ),
            Transaction(
                ammount = 750.50,
                sender = 2,
                senderSurname = "Horak",
                senderName = "Petr",
                recipientAccount = 1,
                recipientName = "Jan",
                recipientSurname = "Novak",
                description = "Vrácení peněz",
                isIncoming = true,
                title = "Vrácení"
            ),
            Transaction(
                ammount = 200.00,
                sender = 3,
                senderSurname = "Svoboda",
                senderName = "Karel",
                recipientAccount = 1,
                recipientName = "Jan",
                recipientSurname = "Novak",
                description = "Dárek",
                isIncoming = true,
                title = "Dárek"
            )
        )

        // Asynchronní přidání transakcí do databáze
        transactions.forEach { transaction ->
            addTransaction(transaction)
        }
    }
    fun addTransaction(transaction: Transaction){
        viewModelScope.launch(Dispatchers.IO) {
            //dispatchers.io používá oddělené vlákno rpo zápis/čtení
            transactionRepository.addTransaction(transaction = transaction)
        }
    }
    fun createAndAddTransaction() {
        transactionRecipientAccount = transactionRecipientAccountString.toInt()
        if (transactionAmount > userAcctState){
            return
        }
        if (transactionAmount <= 0) {
            throw IllegalArgumentException("Suma musí být větší, než 0.")
        }
        if ( transactionRecipientAccount <= 0) {
            throw IllegalArgumentException("id adresáta musí být platné.")
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val timestampModified = dateFormat.format(Date())  // Vytvoří timestamp ve formátu "yyyy-MM-dd'T'HH:mm"

        val newTransaction = Transaction(
            ammount = transactionAmount,
            sender = transactionSender,
            senderSurname = transactionSenderSurname,
            senderName = transactionSenderName,
            recipientAccount = transactionRecipientAccount,
            recipientName = transactionRecipientName,
            recipientSurname = transactionRecipientSurname,
            description = transactionDescriptionChanged.trim(),
            isIncoming = transactionIsIncoming,
            title = transactionTitleState.trim(),
            timeStamp   = timestampModified  // Aktuální čas v milisekundách
        )
        addTransaction(newTransaction)
        viewModelScope.launch {  transactionRepository.executeTransaction(newTransaction) }

    }

    fun getATransactionById(id:Long):Flow<Transaction>{
        return transactionRepository.getATransactionById(id)
    }

    fun updateTransaction(transaction: Transaction){
        viewModelScope.launch(Dispatchers.IO) {
            //dispatchers.io používá oddělené vlákno rpo zápis/čtení
            transactionRepository.updateATransaction(transaction = transaction)
        }
    }


    //technicky vzato by se v bakovní appce něměly transakce mazat
    fun deleteTransaction(transaction: Transaction){
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.deleteATransaction(transaction = transaction)
        }
    }

    fun loginUser(login: String, password: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            viewModelScope.launch {
                transactionRepository.getPersonByLogin(login).collect { person ->
                    if (person != null && person.passwd == password) {
                        loggedInUser = person
                        //aktualizace dat odesilatele pro funkci
                        transactionSender = person.acctId.toInt()
                        transactionSenderName = person.name
                        transactionSenderSurname = person.secondName
                        userAcctState = person.balance
                        userAcctNum = person.acctId.toInt()
                        getAllTransactionsByUserId = transactionRepository.getTransactionsByUser(userAcctNum)

                        // uspesny login
                        onSuccess()
                    } else {
                        // neuspesny login
                        onError()
                    }
                }
            }
        }
    }
    fun onRecipientAccountChanged(accountId: Long) {
        transactionRecipientAccount = accountId.toInt()
        viewModelScope.launch {
            transactionRepository.getPersonById(accountId).collect { person ->
                if (person != null) {
                    transactionRecipientName = person.name
                    transactionRecipientSurname = person.secondName
                    userAcctState = person.balance

                } else {
                    transactionRecipientName = ""
                    transactionRecipientSurname = ""
                    // Zobrazit chybovou zprávu uživateli
                }
            }
        }
    }

    fun checkBalance(accountId: Long) {
        viewModelScope.launch {
            try {
                val balance = transactionRepository.getBalance(accountId)
                // Nyní máte zůstatek a můžete ho použít k další logice
            } catch (e: Exception) {
                // Ošetření chyb, např. logování nebo zobrazení chybové zprávy
            }
        }
    }
    fun LogIn(){
        isLoggedIn.value = true
    }
    fun LogOut(){
        isLoggedIn.value = false
    }
    fun resetParameters(){
        transactionTitleState = ""
        transactionDescriptionChanged =""
        transactionAmount = 0.0
        transactionSender = 0
        transactionSenderName = ""
        transactionSenderSurname = ""
        transactionRecipientAccount =0 // ID účtu příjemce
        transactionRecipientName =""
        transactionRecipientSurname =""
        transactionIsIncoming = false
        transactionRecipientAccountString = ""

    }
    fun getPersonsExceptCurrent(): Flow<List<Person>> {
        return transactionRepository.getAllPersons().map { persons ->
            persons.filter { it.acctId != loggedInUser?.acctId }
        }
    }
    fun fetchRecipientDetails(accountId: Int, onDetailsFetched: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val person = transactionRepository.getPersonById(accountId.toLong()).firstOrNull()
            person?.let {
                transactionRecipientName = it.name
                transactionRecipientSurname = it.secondName
                onDetailsFetched()
            } ?: run {
                // Zpracování chyby, pokud osoba neexistuje
                // todo Můžeme například ukázat nějaké chybové hlášení
            }
        }
    }
}



