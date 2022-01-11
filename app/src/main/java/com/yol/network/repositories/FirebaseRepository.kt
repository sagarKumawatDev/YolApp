package com.yol.network.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.yol.network.request.RegisterRequest

class FirebaseRepository(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference,
) {

    suspend fun getUserDetailById(id: String): Task<DataSnapshot>? =
        try {
            databaseReference.child("users").child(id).get().addOnCompleteListener {
              it.result.getValue(RegisterRequest::class.java)
            }
        } catch (e: Exception) {
            null
        }
}