package com.moviles.proyecto1.model


import com.google.firebase.firestore.DocumentId
import java.io.Serializable


data class Inventory(
    @DocumentId
    var id: String = "",
    val name: String = "",
    val price: Float = 0f,
    val quantity: Int = 0,
    val total: Float? = null ): Serializable
