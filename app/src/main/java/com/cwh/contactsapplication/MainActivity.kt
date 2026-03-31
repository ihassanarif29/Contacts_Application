package com.cwh.contactsapplication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.cwh.contactsapplication.model.ContactDatabase
import com.cwh.contactsapplication.repository.ContactRepository
import com.cwh.contactsapplication.ui.theme.ContactsApplicationTheme
import com.cwh.contactsapplication.view.AddContactScreen
import com.cwh.contactsapplication.view.ContactDetailScreen
import com.cwh.contactsapplication.view.ContactListScreen
import com.cwh.contactsapplication.view.EditContactDetailScreen
import com.cwh.contactsapplication.viewmodel.ContactViewModel
import com.cwh.contactsapplication.viewmodel.ContactViewModelFactory
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(applicationContext, ContactDatabase::class.java, "contact_database").build()
        val repository = ContactRepository(database.contactDao())
        val viewModel:  ContactViewModel by viewModels { ContactViewModelFactory(repository) }


        //enableEdgeToEdge()
        setContent {
            ContactsApplicationTheme {
                val navController = rememberNavController()
                //AddContactScreen(viewModel, navController = navController)
                NavHost(navController = navController, startDestination = "contactList"){
                    composable("contactList"){
                        ContactListScreen(viewModel, navController)
                    }
                    composable("addContact"){
                        AddContactScreen(viewModel, navController)
                    }
                    composable("contactDetail/{contactId}"){backStackEntry ->
                        val contactId = backStackEntry.arguments?.getString("contactId")?.toInt()
                        val contact = viewModel.allContacts.observeAsState(initial = emptyList()).value.find {
                            it.id == contactId
                        }
                        contact?.let { ContactDetailScreen(it, viewModel, navController) }
                    }
                    composable("editContact/{contactId}"){backStackEntry ->
                        val contactId = backStackEntry.arguments?.getString("contactId")?.toInt()
                        val contact = viewModel.allContacts.observeAsState(initial = emptyList()).value.find {
                            it.id == contactId
                        }
                        contact?.let { EditContactDetailScreen(it, viewModel, navController) }
                    }
                }
            }
        }
    }
}

fun copyUriToInternalStorage(context: Context, uri: Uri, fileName: String): String?{
    val file = File(context.filesDir, fileName)
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            file.absolutePath
        }
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}
