package by.roadstatistics.database.firebase

import android.util.Log
import by.roadstatistics.utils.Constants.FIREBASE_DATABASE_KEY
import by.roadstatistics.utils.Constants.USER_ID
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

/**
 * Repository for connect the Firebase Realtime Database
 */

class FirebaseRepository {

    private val mainScope = CoroutineScope(Dispatchers.Main + Job())
    private val fireDatabase = FirebaseDatabase.getInstance().reference

    fun putUserDoDatabase(id: String, name: String) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                fireDatabase.child(FIREBASE_DATABASE_KEY).push()
                    .setValue(User(toInt(id), name, null, null))
            }
        }
    }

    // little mapper, increment USER_ID and convert to string again
    private fun toInt(id: String): String {
        USER_ID = (id.toInt() + 1).toString()
        return USER_ID
    }

    // function to update user, to get the user cords
    fun updateChildren(lat: Double, lon: Double) {
        val ref = fireDatabase.child(FIREBASE_DATABASE_KEY)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    if (ds.child("id").value == USER_ID) {
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
                Log.i("FFFF", "Firebase call is cancelled in FirebaseRepository/updateChildren")
            }
        }
        ref.addListenerForSingleValueEvent(valueEventListener)
    }

}