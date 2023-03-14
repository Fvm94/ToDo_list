package com.franciscovm.todolist


import android.app.Application
import com.franciscovm.todolist.data.ItemRoomDatabase

class InventoryApplication : Application(){
    val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this) }
}
