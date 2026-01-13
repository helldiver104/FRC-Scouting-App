package org.waltonrobotics.ScoutingApp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    var currentUser by mutableStateOf<FirebaseUser?>(null)
        private set

    var authError by mutableStateOf<String?>(null)
        private set

    init {
        currentUser = auth.currentUser
    }

    fun signUp(email: String, password: String, onSuccess: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    authError = null
                    onSuccess()
                } else {
                    authError = task.exception?.message ?: "Sign up failed"
                }
            }
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    authError = null
                    onSuccess()
                } else {
                    authError = task.exception?.message ?: "Login failed"
                }
            }
    }

    fun signOut() {
        auth.signOut()
        currentUser = null
    }
}