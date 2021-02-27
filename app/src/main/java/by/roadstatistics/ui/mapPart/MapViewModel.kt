package by.roadstatistics.ui.mapPart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.roadstatistics.database.firebase.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MapViewModel : ViewModel() {

    private val fireDatabase = FirebaseDatabase.getInstance().getReference("Users")

    private val liveCordsMutablyLiveData = MutableLiveData<List<User>>()
    val liveCordsLiveData: LiveData<List<User>> = liveCordsMutablyLiveData

    fun getLiveCords() {
        val firebaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<User>()
                for (ds in snapshot.children) {
                    for (ds2 in ds.children) {
                        Log.i("FFFF", ds2.child("lat").value.toString().plus(" lat"))
                        Log.i("FFFF", ds2.child("id").value.toString().plus(" id"))
                        if (ds2.child("lat").value.toString() != "null" && ds2.child("id").value.toString() != "0") {
                            list.add(
                                User(
                                    ds2.child("id").value.toString(),
                                    ds2.child("name").value.toString(),
                                    ds2.child("lat").value.toString(),
                                    ds2.child("lng").value.toString(),
                                )
                            )
                        }
                    }
                }
                liveCordsMutablyLiveData.value = list
                list.clear()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("FFFF", "Error from getLiveCordsFromFirebase() in FirebaseRepository")
            }

        }
        fireDatabase.root.addValueEventListener(firebaseListener)
    }
}