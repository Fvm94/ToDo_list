package com.franciscovm.todolist.data

import java.io.Serializable

data class Item(
    val date:String,
    val note:String,
    val path:String,
): Serializable {
    constructor():this("","","")
}