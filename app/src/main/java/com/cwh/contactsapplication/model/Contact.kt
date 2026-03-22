package com.cwh.contactsapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val image : String,
    val name: String,
    val phoneNumber: String,
    val email: String
)
