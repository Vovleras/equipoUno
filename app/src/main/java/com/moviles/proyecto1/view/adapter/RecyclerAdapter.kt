package com.moviles.proyecto1.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.moviles.proyecto1.databinding.ItemProductBinding
import com.moviles.proyecto1.model.Inventory
import com.moviles.proyecto1.view.viewholder.InventoryViewHolder



class RecyclerAdapter(private val listInventory:MutableList<Inventory>, private val navController: NavController):
    RecyclerView.Adapter<InventoryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InventoryViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryViewHolder(binding, navController)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val product = listInventory[position]
        holder.setItemInventory(product)
    }

    override fun getItemCount(): Int {
        return listInventory.size
    }


}