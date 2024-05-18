package com.example.pseudobankapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pseudobankapp.PaymentViewModel


@Composable
fun LoginDialog(viewModel: PaymentViewModel,onLoginComplete: ()-> Unit){

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf(false) }
    var context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.login_background))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

       // verticalArrangement = Arrangement.Center
    ) {
        Box(){
            ShowBankLogo()
        }
        Text("Vítejte", style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.ExtraBold,
            color = colorResource(id = R.color.blue)
            )
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Login",color = colorResource(id = R.color.button_color))},
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = colorResource(id = R.color.button_color))
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Heslo",color = colorResource(id = R.color.button_color)) },
            singleLine = true,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),

            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = colorResource(id = R.color.button_color))

        )
        Spacer(modifier = Modifier.height(16.dp))
        //-----------------------------
        Button(
            onClick = {
                viewModel.loginUser(username, password, onSuccess = {
                    onLoginComplete()
                }, onError = {
                    loginError = true
                    Toast.makeText(context, "neplatný login, či uživatelské heslo", Toast.LENGTH_SHORT).show()
                })
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.button_color))
        ) {
            Text("Přihlásit", style = TextStyle(fontSize = 17.sp), fontWeight = FontWeight.Bold, color = Color.White)
        }

        if (loginError) {
            Text("Pokus o přihlášení se nezdařil", color = Color.Red)
        }

    }

}
@Composable
fun ShowBankLogo(){
    Image(painter = painterResource(id = (R.drawable.logo)), contentDescription ="Pseudobank Logo made by Chatgpt "
            )
}