package com.moviles.proyecto1.view.viewholder

import android.os.Bundle
import androidx.navigation.NavController
import com.moviles.proyecto1.R
import androidx.recyclerview.widget.RecyclerView
import com.moviles.proyecto1.databinding.ItemProductBinding
import com.moviles.proyecto1.model.Inventory


class InventoryViewHolder(binding: ItemProductBinding, navController: NavController):
    RecyclerView.ViewHolder(binding.root) {

    val bindingItem = binding
    val navController = navController
    fun setItemInventory(inventory: Inventory) {
        bindingItem.tvName.text = inventory.name
        bindingItem.tvID.text = "id: ${inventory.id.toString()}"
        bindingItem.tvPrice.text = "$ ${inventory.price}"
        //bindingItem.tvQuantity.text = "${inventory.quantity}"

        bindingItem.cvProducts.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("clave", inventory)
            navController.navigate(R.id.action_homeInventory_to_detailProduct, bundle)
        }
    }
}