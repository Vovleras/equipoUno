package com.moviles.proyecto1.di

import com.moviles.proyecto1.repository.InventoryRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface RepositoryEntryPoint {
    fun inventoryRepository(): InventoryRepository
}