package com.moviles.proyecto1.model

data class UserResponse(
    val email: String?="",
    val isRegister:Boolean,
    val message: String
)