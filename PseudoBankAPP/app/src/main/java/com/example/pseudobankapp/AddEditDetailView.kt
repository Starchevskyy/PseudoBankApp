package com.example.pseudobankapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pseudobankapp.R
import com.example.pseudobankapp.data.Person
import com.example.pseudobankapp.data.Transaction
import kotlinx.coroutines.launch


@Composable
fun AddEditDetailView(
    id: Long, viewModel: PaymentViewModel , navController: NavController
){
    val snackMessage = remember{
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    //scope umožňuje použití asynchronních metod
    val scaffoldState = rememberScaffoldState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        //definice textu App Bar na novém okně
        topBar = { AppBarView(title =
        if (id != 0L) stringResource(id = R.string.update_payment) else stringResource(id = R.string.new_payment))
        {
            navController.navigateUp()
            viewModel.LogIn()
        }},
        //bez toho appka nepojede
        scaffoldState = scaffoldState,
        modifier = Modifier.background(colorResource(id = R.color.login_background))

    ){
        Column (modifier = Modifier
            .padding(it)
            .padding(8.dp)
            .wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Spacer(modifier  = Modifier.height(10.dp))


            //od
            PaymentTextField(
                label = "Název transakce",// zprava pro prijemce
                value = viewModel.transactionTitleState,
                onValueChanged = viewModel::onTransactionTitleChanged
            )


            var transactionSenderText by remember { mutableStateOf(viewModel.transactionSender.toString()) }
            Spacer(modifier  = Modifier.height(10.dp))
            PaymentTextField(
                label = "Číslo účtu příjemce",
                value = viewModel.transactionRecipientAccountString,
                onValueChanged = { newValue ->
                    viewModel.transactionRecipientAccountString = newValue
                    viewModel.transactionRecipientAccount = newValue.toInt()
                }

            )
            Spacer(modifier  = Modifier.height(10.dp))

            PaymentTextField(
                label = "Částka",
                value = viewModel.transactionAmount.toString(),
                onValueChanged = { viewModel.transactionAmount = it.toDoubleOrNull() ?: viewModel.transactionAmount }
            )
            Spacer(modifier  = Modifier.height(10.dp))
            PaymentTextField(
                label = "Zpráva pro odesílatele",//zprava rpo odesilatele
                value = viewModel.transactionDescriptionChanged,
                onValueChanged = viewModel::onTransactionDescriptionChanged
            )

            Spacer(modifier  = Modifier.height(10.dp))


            Spacer(modifier  = Modifier.height(10.dp))
            Button(onClick = { viewModel.transactionDescriptionChanged = viewModel.transactionTitleState },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.blue),
                    //contentColor = Color.White
                )) {
                Text(text = "duplikovat", color = Color.White, fontWeight = FontWeight.SemiBold)
                
            }
            var showContacts by remember { mutableStateOf(false) }

            //cudlik
            Button(onClick = {viewModel.fetchRecipientDetails(viewModel.transactionRecipientAccount){}
                if (viewModel.transactionTitleState.isNotEmpty() && viewModel.transactionDescriptionChanged.isNotEmpty()) {
                    showDialog = true
                } else {
                    snackMessage.value = "Vyplnte pole k vytvoření transakce"
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)
                    }
                }
            }, colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.blue)
            )) {
                Text(text = if (id != 0L) stringResource(id = R.string.update_payment) else stringResource(
                    id = R.string.new_payment
                ), color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            if (viewModel.userAcctState<=viewModel.transactionAmount){
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(text = "Rekapitulace transakce", fontWeight = FontWeight.Bold, fontSize = 25.sp, color = Color.Black) },
                        text = {
                            Column {
                                androidx.compose.material.Text(text = "Příjemce:")
                                Spacer(modifier = Modifier
                                    .height(8.dp)
                                    .width(8.dp))
                                Row {
                                    Row {
                                        Text(
                                            "  ${viewModel.transactionRecipientName} ${viewModel.transactionRecipientSurname}",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp,
                                            color = Color.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Row {
                                        Text(
                                            "  ${viewModel.transactionAmount}",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp,
                                            color = Color.Red
                                        )
                                        Text(
                                            " $",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp,
                                            color = Color.Black
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                // Spacer(modifier = Modifier.width(45.dp))
                                Row {
                                    Spacer(modifier = Modifier.width(10.dp))
                                    androidx.compose.material.Text(
                                        generateAccountNumberAndBankCode(),
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Zpráva pro Příjemce:",
                                    fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    androidx.compose.material.Text(
                                        "  ${viewModel.transactionDescriptionChanged}",
                                        color = Color.Black, fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(2.dp))
                                Spacer(modifier = Modifier.height(2.dp))
                                Row {
                                    Column {
                                        Row {
                                            Text(text = "Aktuálně:",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp,
                                                color = Color.Black)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row{
                                            Text(viewModel.userAcctState.toString(),textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 21.sp,
                                                color = Color.Green  )
                                        }

                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Column (){
                                        Row {
                                            Text(text = "")

                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                                                contentDescription = "sipka", modifier =Modifier.size(23.dp),
                                                tint= colorResource(id = R.color.blue)
                                            )
                                        }

                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Column {
                                        Row {
                                            Text(text = "Po transakci:",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 18.sp,
                                                color = Color.Black)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row{
                                            Text((viewModel.userAcctState - viewModel.transactionAmount).toString(), textAlign = TextAlign.Right,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 21.sp,
                                                color = Color.Red )
                                        }
                                    }
                                }
                            }
                        },


                        confirmButton = {

                        },
                        dismissButton = {
                            Button(
                                onClick = { showDialog = false },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red,
                                    contentColor = colorResource(id = R.color.white))

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Zamítnout"
                                )
                                Text("Zamítnout", color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        },
                        backgroundColor = colorResource(id = R.color.alert_background)
                    )
                }

            }else{
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(text = "Rekapitulace transakce", fontWeight = FontWeight.Bold, fontSize = 25.sp, color = Color.Black) },
                        text = {
                            Column {
                                androidx.compose.material.Text(text = "Příjemce:")
                                Spacer(modifier = Modifier
                                    .height(8.dp)
                                    .width(8.dp))
                                Row {
                                    Row {
                                        Text(
                                            "  ${viewModel.transactionRecipientName} ${viewModel.transactionRecipientSurname}",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp,
                                            color = Color.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Row {
                                        Text(
                                            "  ${viewModel.transactionAmount}",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp,
                                            color = Color.Red
                                        )
                                        Text(
                                            " $",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp,
                                            color = Color.Black
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                // Spacer(modifier = Modifier.width(45.dp))
                                Row {
                                    Spacer(modifier = Modifier.width(10.dp))
                                    androidx.compose.material.Text(
                                        generateAccountNumberAndBankCode(),
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Zpráva pro Příjemce:",
                                    fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    androidx.compose.material.Text(
                                        "  ${viewModel.transactionDescriptionChanged}",
                                        color = Color.Black, fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(2.dp))
                                Spacer(modifier = Modifier.height(2.dp))
                                Row {
                                    Column {
                                        Row {
                                            Text(text = "Aktuálně:",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp,
                                                color = Color.Black)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row{
                                            Text(viewModel.userAcctState.toString(),textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 21.sp,
                                                color = Color.Green  )
                                        }

                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Column (){
                                        Row {
                                            Text(text = "")

                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                                                contentDescription = "sipka", modifier =Modifier.size(23.dp),
                                                tint= colorResource(id = R.color.blue)
                                            )
                                        }

                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Column {
                                        Row {
                                            Text(text = "Po transakci:",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 18.sp,
                                                color = Color.Black)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row{
                                            Text((viewModel.userAcctState - viewModel.transactionAmount).toString(), textAlign = TextAlign.Right,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 21.sp,
                                                color = Color.Green )
                                        }
                                    }
                                }
                            }
                        },


                        confirmButton = {
                            Button(onClick = {
                                viewModel.fetchRecipientDetails(viewModel.transactionRecipientAccount) {
                                    viewModel.createAndAddTransaction()
                                    viewModel.resetParameters()
                                    showDialog = false
                                    snackMessage.value = "Transakce byla vytvořena"
                                    scope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)
                                        navController.navigateUp()
                                        viewModel.LogIn()
                                    }
                                }
                            }, colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.blue),
                                contentColor = colorResource(id = R.color.white))
                            )

                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Confirm"
                                )
                                Text("Potvrdit", color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDialog = false },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red,
                                    contentColor = colorResource(id = R.color.white))

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Zamítnout"
                                )
                                Text("Zamítnout", color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        },
                        backgroundColor = colorResource(id = R.color.alert_background)
                    )
                }

            }




            }
    }
}

@Composable
fun PaymentTextField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier =Modifier
){
    OutlinedTextField(value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label, color = Color.Black, fontWeight = FontWeight.SemiBold)},
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            focusedBorderColor = colorResource(id = R.color.button_color),
            unfocusedBorderColor = colorResource(id = R.color.myBlue),
            cursorColor = colorResource(id =R.color.black),
            focusedLabelColor = colorResource(id = R.color.button_color),
            unfocusedLabelColor = colorResource(id = R.color.black)

        )
    )
}
@Composable
fun ContactsView(viewModel: PaymentViewModel, onAccountSelected: (Int) -> Unit) {
    val contacts = viewModel.getPersonsExceptCurrent().collectAsState(initial = emptyList())

    LazyColumn {
        items(contacts.value) { person ->
            ContactItem(person, onAccountSelected)
        }
    }
}

@Composable
fun ContactItem(person: Person, onAccountSelected: (Int) -> Unit) {
    Row(modifier = Modifier
        .clickable { onAccountSelected(person.acctId.toInt()) }
        .padding(16.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${person.name} ${person.secondName}", style = MaterialTheme.typography.h6)
        Spacer(Modifier.weight(1f))
        Text(text = "Vybrat", color = MaterialTheme.colors.primary)
    }
}
