package by.roadstatistics.database.firebase

import android.util.Log
import by.roadstatistics.utils.Constants.FIREBASE_DATABASE_USER_KEY
import by.roadstatistics.utils.Constants.USER_ID
import com.google.firebase.database.*
import kotlinx.coroutines.*

class FirebaseRepository {

    private val mainScope = CoroutineScope(Dispatchers.Main + Job())
    private val fireDatabase = FirebaseDatabase.getInstance().reference

    fun putUserDoDatabase(id: String, name: String) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                fireDatabase.child("Users").push().setValue(User(toInt(id), name, null, null))
            }
        }
    }

    private fun toInt(id: String): String {
        return (id.toInt() + 1).toString()
    }

    fun updateChildren(lat: Double, lon: Double) {
        val ref = fireDatabase.child("Users")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    if (ds.child("id").value == USER_ID) {
                        Log.i("FFFF", "user id = $USER_ID")
                        ref.child(ds.key.toString()).setValue(
                            User(
                                USER_ID,
                                ds.child("name").value.toString(),
                                lat.toString(),
                                lon.toString()
                            )
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        ref.addListenerForSingleValueEvent(valueEventListener)
    }

}