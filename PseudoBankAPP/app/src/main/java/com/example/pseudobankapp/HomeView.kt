package com.example.pseudobankapp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pseudobankapp.R
import com.example.pseudobankapp.Screen
import com.example.pseudobankapp.data.Transaction
import com.example.pseudobankapp.LoginDialog
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Random


@Composable
fun HomeView(navController: NavController, viewModel: PaymentViewModel){
    var name: String = " Tomas"
    var secondName: String ="Fuk"
    var isLogged: Boolean = false
    var balance: Double = 1300.0
    var accountNumber: Int = 4521
    val context = LocalContext.current
    var showLogin by remember { mutableStateOf(true) }
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }

    var loggedIn by remember{ mutableStateOf(false) }
    if(viewModel.isLoggedIn.value){
        showLogin = false
        loggedIn = true
    }

    if (showLogin){
        LoginDialog(viewModel) {
            loggedIn = true
            showLogin = false
        }
    }

    if(loggedIn && viewModel.loggedInUser != null ){
        Scaffold (
            topBar = {AppBarView(title = "PseudoBank",{
                Toast.makeText(context, "tlacitko je stisknuto", Toast.LENGTH_LONG).show()

            } )},
            //přidání floating action button
            floatingActionButton = { FloatingActionButton(
                modifier = Modifier.padding(all = 30.dp),
                contentColor = Color.White,
                backgroundColor = colorResource(id = R.color.blue),

                onClick = {
                    navController.navigate(Screen.AddScreen.route)
                    }) {
                Icon(imageVector =Icons.Default.Add, contentDescription = null)
            }}
        ){
            val user = viewModel.loggedInUser!!
            //val transactionList=viewModel.getAllTransactions.collectAsState(initial = listOf() )
            val transactionList=viewModel.getAllTransactionsByUserId.collectAsState(initial = listOf() )
            //natáhnutí transakcí z databáze
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(it)){
                item { UserCard(name = user.name, secondName = user.secondName , balance = user.balance, accountNumber = user.acctId.toInt()) }
                items(transactionList.value){
                    transaction -> TransactionItem(transaction = transaction, userID = user.acctId.toInt()) {
                    selectedTransaction = transaction
                }
                }
                // volání predpripravenych plateb
                /*
                items(DummyPaymnet.paymentList){
                        payment -> PaymentItem(payment = payment) {
                }
                */
            }
            selectedTransaction?.let { transaction ->
                var isTansactionIncoming  = false
                if(transaction.recipientAccount == user.acctId.toInt()){isTansactionIncoming = true}else {isTansactionIncoming = false}
                if (isTansactionIncoming){
                    AlertDialog(
                        onDismissRequest = { selectedTransaction = null },
                        title = { Text("Příchozí platba", fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, color = Color.Black, textAlign = TextAlign.Center) },
                        text = {
                            Column {
                                Text(text = "Od:")
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Row {
                                        Text("  ${transaction.senderName} ${transaction.senderSurname}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color.Black)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Row {
                                        Text("  ${transaction.ammount}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color.Green)
                                        Text(" $", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color.Black)
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                               // Spacer(modifier = Modifier.width(45.dp))
                                Row {
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(generateAccountNumberAndBankCode(), color = Color.Black, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Box(){
                                        Row(modifier = Modifier.align(Alignment.Center)){
                                            Text(text = extractDate(transaction.timeStamp), color = Color.Black, fontWeight = FontWeight.SemiBold).toString()
                                            Spacer(modifier = Modifier.width(1.dp))
                                            Text(text = ":")
                                            Spacer(modifier = Modifier.width(1.dp))
                                            Text(text = extractTime(transaction.timeStamp)).toString()
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Zpráva pro Příjemce:")
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Text("  ${transaction.title}", color = Color.Black)
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = { selectedTransaction = null }, colors = ButtonDefaults.buttonColors(
                                    backgroundColor = colorResource(id = R.color.blue),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(imageVector = Icons.Filled.Check, contentDescription = "confirm")
                            }
                        }
                    )

                }else{
                    AlertDialog(
                        onDismissRequest = { selectedTransaction = null },
                        title = { Text("Odchozí platba", fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, color = Color.Black, textAlign = TextAlign.Center) },
                        text = {
                            Column {
                                Text(text = "Pro:")
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Row {
                                        Text("  ${transaction.recipientName} ${transaction.recipientSurname}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color.Black)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Row {
                                        Text("  ${transaction.ammount}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color.Red)
                                        Text(" $", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color.Black)
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                // Spacer(modifier = Modifier.width(45.dp))
                                Row {
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(generateAccountNumberAndBankCode(), color = Color.Black, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Box(){
                                        Row(modifier = Modifier.align(Alignment.Center)){
                                            Text(text = extractDate(transaction.timeStamp), color = Color.Black, fontWeight = FontWeight.SemiBold).toString()
                                            Spacer(modifier = Modifier.width(1.dp))
                                            Text(text = ":")
                                            Spacer(modifier = Modifier.width(1.dp))
                                            Text(text = extractTime(transaction.timeStamp)).toString()
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Zpráva pro Příjemce:")
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Text("  ${transaction.description}", color = Color.Black)
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = { selectedTransaction = null }, colors = ButtonDefaults.buttonColors(
                                    backgroundColor = colorResource(id = R.color.blue),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(imageVector = Icons.Filled.Check, contentDescription = "confirm")
                            }
                        }
                    )
                }
            }
        }
    }
    }


//prototyp zobrazení transkací
@Composable
fun TransactionItem(transaction: Transaction,userID:Int, onClick: () -> Unit
                    /*onItemClick:(Transaction) ->Unit */ ){
//fun PaymentItem(payment: Payment, onClick: () -> Unit  ){
    Card(modifier = Modifier
        .fillMaxWidth()
        .background(colorResource(id = R.color.login_background))
        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
        .clickable { onClick() }, elevation = 10.dp, backgroundColor = Color.White) {
        Column(modifier = Modifier.padding(16.dp)) {
            // volání obsahu z data třídy Payment

            Row {
                Column {
                        Row {
                            if (transaction.recipientAccount == userID) {
                                Text(text= transaction.senderName, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.width(4.dp) )
                                Text(text= transaction.senderSurname, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.width(8.dp) )

                            }else{
                                Text(text= transaction.recipientName, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.width(4.dp) )
                                Text(text= transaction.recipientSurname, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.width(8.dp) )
                            }

                            Box(){
                                Row(modifier = Modifier.align(Alignment.Center)){
                                    Text(text = extractDate(transaction.timeStamp),).toString()
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(text = " : ")
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(text = extractTime(transaction.timeStamp)).toString()
                                }
                            }
                        }

                    if (transaction.recipientAccount == userID){
                        Row {
                           // Text(text = "zprava rpo prijemce ")
                            Text(text= transaction.title, fontWeight = FontWeight.Normal)
                        }

                    }else{
                        Row {
                           // Text(text = "zprava rpo odesilatele ")
                            Text(text= transaction.description , fontWeight = FontWeight.Normal)
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Row {
                        Text(text= transaction.ammount.toString(), fontWeight = FontWeight.ExtraBold,
                            color = if (transaction.recipientAccount == userID) Color.Green else Color.Red
                              )
                        Spacer(modifier = Modifier.width(2.dp) )
                        ShowCoinIcon()
                    }
                }
            }
        }
    }
}
@Composable
fun UserCard(name: String, secondName: String, balance: Double,accountNumber: Int){
    val  configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val cardHeight = screenHeight * 0.25f
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(cardHeight)
        .background(Color(0xFFE6FAFF))
        .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 15.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor =  colorResource(id = R.color.cardColor),
        border = BorderStroke(0.1.dp, Color.LightGray)
    ){
        Box(){
            Column {
                Spacer(modifier = Modifier.height(cardHeight*0.05f))
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Debetní karta",
                        style = TextStyle(fontSize = 19.sp),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "VIZA",
                        style = TextStyle(fontSize = 23.sp),
                        fontWeight = FontWeight.Bold,
                        color = Color.White)
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Spacer(modifier = Modifier.height(1.dp))
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    ShowRocketIcon()
                    Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.width(16.dp))

                Spacer(modifier = Modifier.height(cardHeight*0.009f))

                Row {
                    Column {
                        Row {
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = name,
                                style = TextStyle(fontSize = 19.sp),
                                fontWeight = FontWeight.Bold,
                                color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp) )
                            Text(text = secondName,
                                style = TextStyle(fontSize = 19.sp),
                                fontWeight = FontWeight.Bold,
                                color = Color.White)
                        }
                        Row {
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = " No  ",
                                style = TextStyle(fontSize = 14.sp),
                                fontWeight = FontWeight.Medium,
                                color = Color.White)
                            Text(accountNumber.toString(),
                                style = TextStyle(fontSize = 14.sp),
                                fontWeight = FontWeight.Medium,
                                color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = balance.toString(),
                        style = TextStyle(fontSize = 20.sp),
                        fontWeight = FontWeight.ExtraBold,
                        color = colorResource(id = R.color.white)
                    )
                    Spacer(modifier = Modifier.width(2.dp) )
                    ShowCoinBagIcon()
                    Spacer(modifier = Modifier.width(8.dp) )
                }
                Row {
                    Text(text = "NO")
                }
            }
            Spacer(modifier = Modifier.width(35.dp))

        }
    }
}


@Composable
fun ShowCoinIcon(){
    Image(painter = painterResource(id = (R.drawable.money_icon)), contentDescription ="dolar coin handwritten icon downloaded from iconpricker " )
}
@Composable
fun ShowCoinBagIcon(){
    Image(painter = painterResource(id = (R.drawable.moneybag_icon)), contentDescription ="Moneybag handwritten icon downloaded from iconpricker ",
        modifier = Modifier.size(50.dp))
}
@Composable
fun ShowRocketIcon(){
    Image(painter = painterResource(id = (R.drawable.rocket)), contentDescription ="Moneybag handwritten icon downloaded from iconpricker ",
        modifier = Modifier
            .size(100.dp)
            .rotate(30f))
}
fun extractDate(timestamp: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(timestamp)  // Převede timestamp string na Date objekt
    return outputFormat.format(date)  // Formátuje Date objekt na string ve formátu YYYY-MM-DD
}
fun extractTime(timestamp: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val date = inputFormat.parse(timestamp)  // Převede timestamp string na Date objekt
    return outputFormat.format(date)  // Formátuje Date objekt na string ve formátu HH:mm
}

fun generateAccountNumberAndBankCode(): String {
    val accountNumber = Random().ints(10L, 0, 10)
        .toArray()
        .joinToString("")
    val bankCode = Random().ints(3L, 0, 10)
        .toArray()
        .joinToString("")
    return "$accountNumber/$bankCode"
}