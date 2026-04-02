package com.cwh.contactsapplication.view

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cwh.contactsapplication.R
import com.cwh.contactsapplication.copyUriToInternalStorage
import com.cwh.contactsapplication.ui.theme.MyCustomColor
import com.cwh.contactsapplication.viewmodel.ContactViewModel



fun isValidName(name: String): Boolean {
    return name.trim().matches(Regex("^[a-zA-Z ]{2,50}$"))
}

fun isValidPhone(phone: String): Boolean {
    return phone.matches(Regex("^\\+?[0-9]{10,15}$"))
}

fun isValidEmail(email: String): Boolean {
    return email.isEmpty() || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen(viewModel: ContactViewModel, navController: NavController){
    val context = LocalContext.current.applicationContext
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }


    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    //Content picker in Jetpack Compose
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){uri: Uri? ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(48.dp),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(Alignment.CenterVertically)
                    ) {
                        Text(text = "Add Contact", fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                        Toast.makeText(context,"Add Contact", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(painter = painterResource(R.drawable.add_contact),contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MyCustomColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            imageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                //Open only Images no Videos PDF etc
                onClick = {launcher.launch("image/*")},
                colors = ButtonDefaults.buttonColors(MyCustomColor)
            ) {
                Text(text = "Chose Image")
            }

            Spacer(modifier = Modifier.height(18.dp))

            TextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null},
                label = { Text( text = "Name") },
                isError = nameError != null,
                supportingText = {
                    nameError?.let { Text(it, color = Color.Red) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                    phoneError = null },
                label = { Text( text = "Phone Number") },
                isError = phoneError != null,
                supportingText = {
                    phoneError?.let { Text(it, color = Color.Red) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null},
                label = { Text( text = "Email") },
                isError = emailError != null,
                supportingText = {
                    emailError?.let { Text(it, color = Color.Red) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    var isValid = true

                    if (!isValidName(name)) {
                        nameError = "Enter valid name (only letters)"
                        isValid = false
                    }

                    if (!isValidPhone(phoneNumber)) {
                        phoneError = "Enter valid phone (10–15 digits)"
                        isValid = false
                    }

                    if (!isValidEmail(email)) {
                        emailError = "Enter valid email"
                        isValid = false
                    }

                    if (imageUri == null) {
                        Toast.makeText(context, "Please select image", Toast.LENGTH_SHORT).show()
                        isValid = false
                    }

                    if (!isValid) return@Button

                    // ✅ Save if valid
                    imageUri?.let {
                        val internalPath = copyUriToInternalStorage(context, it, "$name.jpg")
                        internalPath?.let { path ->
                            viewModel.addContact(path, name.trim(), phoneNumber.trim(), email.trim())
                            navController.navigate("contactList") {
                                popUpTo(0)
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(MyCustomColor)
            ) {
                Text(text = "Add Contact")
            }
        }
    }
}