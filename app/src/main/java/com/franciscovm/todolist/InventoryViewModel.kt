package com.franciscovm.todolist

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franciscovm.todolist.data.Item
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val error_tag = "ViewModelError"

class InventoryViewModel() : ViewModel() {

    private val fireStore = Firebase.firestore.collection("user")
    private var list: MutableLiveData<List<Item>> = MutableLiveData()

    val queryDirection = Query.Direction.ASCENDING
    val fieldFilter = "date"

    val lista: LiveData<List<Item>> = getFromFirebase()
    private fun getFromFirebase(): LiveData<List<Item>> {
        viewModelScope.launch {
            fireStore.orderBy(fieldFilter, queryDirection)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.e(error_tag, error.message.toString())
                    }
                    list.value = value!!.toObjects(Item::class.java)
                }

        }
        return list
    }

    private fun addToFirebase(item: Item, context: Context) {
        fireStore.document(item.path).set(item).addOnSuccessListener {
            Toast.makeText(context, "Saved on Firebase", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getEntryToFirebase(note: String): Item {

        val formatData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val current = LocalDateTime.now().format(formatData)

        return Item(current, note, fireStore.document().id)
    }

    fun saveOnFirebase(note: String, context: Context) {
        addToFirebase(getEntryToFirebase(note), context)
    }

    fun isEntryValid(noteText: String): Boolean {
        return noteText.isNotEmpty()
    }

}
