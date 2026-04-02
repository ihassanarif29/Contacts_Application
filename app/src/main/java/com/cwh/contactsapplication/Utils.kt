package com.cwh.contactsapplication

class Utils {

    fun isValidName(name: String): Boolean {
        return name.trim().matches(Regex("^[a-zA-Z ]{2,50}$"))
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regex("^\\+?[0-9]{10,15}$"))
    }

    fun isValidEmail(email: String): Boolean {
        return email.isEmpty() || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}