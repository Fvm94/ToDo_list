package com.franciscovm.todolist.data

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class InventoryViewModel(private val dao: Dao):ViewModel() {

    private fun insertItem(item:Item){
        viewModelScope.launch {
            dao.insert(item)
        }
    }

    fun deleteItem(item: Item){
        viewModelScope.launch {
            dao.delete(item)
        }
    }

    val allItems = dao.getItems().asLiveData()

    private fun getNewWEntry(item:String):Item{
        return Item(
             item = item)
    }

    fun addNewEntry(item: String) = insertItem(getNewWEntry(item))
    fun isEntryValid(text: String): Boolean {
        if(text.isBlank()){
            return false
        }
        return true
    }


    fun retrieveItem(id: Int): LiveData<Item>{
        return dao.getItem(id).asLiveData()
    }

}
class InventoryViewModelFactory(private val dao: Dao):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}